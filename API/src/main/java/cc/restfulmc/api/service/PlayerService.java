package cc.restfulmc.api.service;

import cc.restfulmc.api.common.*;
import cc.restfulmc.api.common.web.JsonWebException;
import cc.restfulmc.api.common.web.JsonWebRequest;
import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.MojangRateLimitException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.Player;
import cc.restfulmc.api.model.ProfileAction;
import cc.restfulmc.api.model.cache.CachedPlayer;
import cc.restfulmc.api.model.cache.CachedPlayerName;
import cc.restfulmc.api.model.cache.CachedSkinPartTexture;
import cc.restfulmc.api.model.skin.Skin;
import cc.restfulmc.api.model.skin.SkinRendererType;
import cc.restfulmc.api.model.token.mojang.MojangProfileToken;
import cc.restfulmc.api.model.token.mojang.MojangUsernameToUUIDToken;
import cc.restfulmc.api.repository.PlayerCacheRepository;
import cc.restfulmc.api.repository.PlayerNameCacheRepository;
import cc.restfulmc.api.repository.SkinPartTextureCacheRepository;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Braydon
 */
@Service @Log4j2(topic = "Player Service")
public final class PlayerService {
    public static PlayerService INSTANCE;

    private static final String UUID_TO_PROFILE = MojangServer.SESSION.getEndpoint() + "/session/minecraft/profile/%s";
    private static final String USERNAME_TO_UUID = MojangServer.API.getEndpoint() + "/users/profiles/minecraft/%s";

    private static final int DEFAULT_PART_TEXTURE_SIZE = 128;
    private static final int MAX_PART_TEXTURE_SIZE = 512;

    /**
     * The cache repository for {@link Player}'s by their username.
     */
    @NonNull private final PlayerNameCacheRepository playerNameCache;

    /**
     * The cache repository for {@link Player}'s.
     */
    @NonNull private final PlayerCacheRepository playerCache;

    /**
     * The cache repository for {@link SkinRendererType}'s.
     */
    @NonNull private final SkinPartTextureCacheRepository skinPartTextureCache;

    @Autowired
    public PlayerService(@NonNull PlayerNameCacheRepository playerNameCache, @NonNull PlayerCacheRepository playerCache,
                         @NonNull SkinPartTextureCacheRepository skinPartTextureCache) {
        INSTANCE = this;
        this.playerNameCache = playerNameCache;
        this.playerCache = playerCache;
        this.skinPartTextureCache = skinPartTextureCache;
    }

    /**
     * Get a player by their username or UUID.
     * <p>
     * If the player is present within the cache, that will
     * be returned. If the player is not cached, a request
     * will be made to retrieve the player from Mojang, cache it
     * and then return the response.
     * </p>
     *
     * @param query  the query to search for the player by
     * @param signed whether the profile is signed
     * @return the player
     * @throws BadRequestException       if the UUID or username is invalid
     * @throws ResourceNotFoundException if the player is not found
     * @throws MojangRateLimitException  if the Mojang API rate limit is reached
     */
    @NonNull
    public CachedPlayer getPlayer(@NonNull String query, boolean signed) throws BadRequestException, ResourceNotFoundException, MojangRateLimitException {
        log.info("Requesting player with query: {}", query);

        UUID uuid; // The player UUID to lookup
        boolean isFullUuid = query.length() == 36; // Was a UUID provided?
        if (query.length() == 32 || isFullUuid) { // Parse the query as a UUID
            try {
                uuid = isFullUuid ? UUID.fromString(query) : UUIDUtils.addDashes(query);
                log.info("Parsed {}UUID: {} -> {}", isFullUuid ? "" : "trimmed ", query, uuid);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Malformed UUID provided: %s".formatted(query));
            }
        } else { // The query is a username, request from Mojang
            if (!MiscUtils.isUsernameValid(query)) { // Ensure the username is valid
                throw new BadRequestException("Invalid username provided: %s".formatted(query));
            }
            uuid = usernameToUUID(query);
            log.info("Found UUID for username {}: {}", query, uuid);
        }
        String cacheKey = "%s-%s".formatted(uuid, signed); // The cache id of the player

        // Check the cache for the player
        // and return it if it's present
        Optional<CachedPlayer> cached = playerCache.findById(cacheKey);
        if (cached.isPresent()) { // Respond with the cache if present
            log.info("Found player in cache: {}", uuid);
            return cached.get();
        }

        // Send a request to Mojang requesting
        // the player profile by their UUID
        try {
            log.info("Retrieving player profile for UUID: {}", uuid);
            String endpoint = UUID_TO_PROFILE.formatted(uuid) + (signed ? "?unsigned=false" : "");
            MojangProfileToken token = JsonWebRequest.makeRequest(endpoint, HttpMethod.GET).execute(MojangProfileToken.class);
            MojangProfileToken.SkinProperties skinProperties = token.getSkinProperties(); // Get the skin and cape
            ProfileAction[] profileActions = token.getProfileActions();

            // Build our player model, cache it, and then return it
            CachedPlayer player = new CachedPlayer(cacheKey, uuid, token.getName(),
                    skinProperties.getSkin() == null ? Skin.DEFAULT_STEVE : skinProperties.getSkin(),
                    skinProperties.getCape(), token.getProperties(), profileActions.length == 0 ? null : profileActions,
                    token.isLegacy(), System.currentTimeMillis()
            );
            // Store in the cache
            playerCache.save(player);
            log.info("Cached player: {}", uuid);

            player.setCached(-1L); // Set to -1 to indicate it's not cached in the response
            return player;
        } catch (JsonWebException ex) {
            // No profile found, return null
            if (ex.getStatusCode() == 204 || ex.getStatusCode() == 400) {
                throw new ResourceNotFoundException("Player not found with query: %s".formatted(query));
            }
            throw ex;
        }
    }

    /**
     * Gets the skin image for the given skin.
     *
     * @param skinUrl the skin url to get the image for
     * @return the skin image
     */
    public byte[] getSkinTexture(@NonNull String skinUrl, boolean upgrade) {
        // Get the bytes of the skin and instantly
        // return if we're not upgrading the skin
        byte[] skinBytes = ImageUtils.getImage(skinUrl);
        if (!upgrade) {
            return skinBytes;
        }
        // Handle upgrading the skin if necessary
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Objects.requireNonNull(skinBytes))) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null || (image.getWidth() != 64 || image.getHeight() != 32)) {
                return skinBytes;
            }
            long start = System.currentTimeMillis();
            BufferedImage upgraded = SkinUtils.upgradeLegacySkin(image);
            byte[] bytes = ImageUtils.toByteArray(upgraded);
            log.debug("Upgraded legacy skin '{}' in {}ms", skinUrl, System.currentTimeMillis() - start);
            return bytes;
        } catch (Exception e) {
            log.warn("Could not upgrade legacy skin, using original: {}", e.getMessage());
        }
        return skinBytes;
    }

    /**
     * Get the part of a skin texture for
     * a player by their username or UUID.
     *
     * @param partName   the part of the player's skin texture to get
     * @param query      the query to search for the player by
     * @param extension  the skin part image extension
     * @param overlays   whether to render overlays
     * @param sizeString the size of the skin part image
     * @return the skin part texture
     * @throws BadRequestException      if the extension is invalid
     * @throws MojangRateLimitException if the Mojang API rate limit is reached
     */
    @SneakyThrows
    public byte[] getSkinPartTexture(@NonNull String partName, @NonNull String query, @NonNull String extension,
                                     boolean overlays, String sizeString) throws BadRequestException, MojangRateLimitException {
        log.info("Requesting skin part {} with query {} (ext: {}, overlays: {}, size: {})",
                partName, query, extension, overlays, sizeString
        );

        // Get the part from the given name
        SkinRendererType part = EnumUtils.getEnumConstant(SkinRendererType.class, partName.toUpperCase());
        if (part == null) {
            part = SkinRendererType.FACE;
            log.warn("Invalid skin part {}, defaulting to {}", partName, part.name());
        }

        // Ensure the extension is valid
        if (extension.isBlank()) {
            throw new BadRequestException("Invalid extension");
        }

        // Get the size of the part
        Integer size = null;
        if (sizeString != null) { // Attempt to parse the size
            try {
                size = Integer.parseInt(sizeString);
            } catch (NumberFormatException ignored) {
                // Safely ignore, invalid number provided
            }
        }
        if (size == null || size <= 0) { // Invalid size
            size = DEFAULT_PART_TEXTURE_SIZE;
            log.warn("Invalid size {}, defaulting to {}", sizeString, size);
        }
        if (size > MAX_PART_TEXTURE_SIZE) { // Limit the size to 512
            size = MAX_PART_TEXTURE_SIZE;
            log.warn("Size {} is too large, defaulting to {}", sizeString, MAX_PART_TEXTURE_SIZE);
        }
        String cacheKey = "%s-%s-%s-%s-%s".formatted(query.toLowerCase(), part.name(), overlays, size, extension); // The id of the skin part

        // In production, check the cache for the
        // skin part and return it if it's present
        if (EnvironmentUtils.isProduction()) {
            Optional<CachedSkinPartTexture> cached = skinPartTextureCache.findById(cacheKey);
            if (cached.isPresent()) { // Respond with the cache if present
                log.info("Found skin part {} in cache: {}", part.name(), cacheKey);
                return cached.get().getTexture();
            }
        }

        Skin skin = null; // The target skin to get the skin part of
        long before = System.currentTimeMillis();
        try {
            skin = getPlayer(query, false).getSkin(); // Use the player's skin
        } catch (Exception ignored) {
            // Simply ignore, and fallback to the default skin
        }
        if (skin == null) { // Fallback to the default skin
            skin = Skin.DEFAULT_STEVE;
            log.warn("Failed to get skin for player {}, defaulting to Steve", query);
        } else {
            log.info("Got skin for player {} in {}ms", query, System.currentTimeMillis() - before);
        }
        before = System.currentTimeMillis();
        BufferedImage texture = part.getRenderer().render(skin, overlays, size); // Render the skin part
        log.info("Render of skin part took {}ms: {}", System.currentTimeMillis() - before, cacheKey);

        byte[] bytes = ImageUtils.toByteArray(texture); // Convert the image into a byte array
        skinPartTextureCache.save(new CachedSkinPartTexture(cacheKey, bytes)); // Cache the texture
        log.info("Cached skin part texture: {}", cacheKey);
        return bytes;
    }

    /**
     * Get the UUID of a player by their username.
     *
     * @param username the player's username
     * @return the player's UUID
     * @throws ResourceNotFoundException if the player isn't found
     * @throws MojangRateLimitException  if the Mojang rate limit is reached
     */
    @NonNull
    private UUID usernameToUUID(@NonNull String username) throws ResourceNotFoundException, MojangRateLimitException {
        String originalUsername = username;
        username = username.toLowerCase(); // Lowercase the username

        // Check the cache for the player's UUID
        Optional<CachedPlayerName> cached = playerNameCache.findById(username);
        if (cached.isPresent()) { // Respond with the cache if present
            log.info("Found UUID in cache for username {}: {}", originalUsername, cached.get().getUniqueId());
            return cached.get().getUniqueId();
        }

        // Make a request to Mojang requesting the UUID
        try {
            MojangUsernameToUUIDToken token = JsonWebRequest.makeRequest(
                    USERNAME_TO_UUID.formatted(username), HttpMethod.GET
            ).execute(MojangUsernameToUUIDToken.class);

            // Cache the UUID and return it
            UUID uuid = UUIDUtils.addDashes(token.getId());
            playerNameCache.save(new CachedPlayerName(username, uuid));
            log.info("Cached UUID for username {}: {}", username, uuid);
            return uuid;
        } catch (JsonWebException ex) {
            if (ex.getStatusCode() == 429) { // Mojang rate limit reached
                throw new MojangRateLimitException();
            } else if (ex.getStatusCode() == 404) { // Player not found
                throw new ResourceNotFoundException("Player not found with username: %s".formatted(originalUsername));
            }
            throw ex;
        }
    }
}