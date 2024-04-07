package me.braydon.mc.service;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.common.DNSUtils;
import me.braydon.mc.common.EnumUtils;
import me.braydon.mc.common.Tuple;
import me.braydon.mc.common.UUIDUtils;
import me.braydon.mc.common.web.JsonWebException;
import me.braydon.mc.common.web.JsonWebRequest;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.InvalidMinecraftServerPlatform;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.*;
import me.braydon.mc.model.cache.CachedPlayer;
import me.braydon.mc.model.cache.CachedPlayerName;
import me.braydon.mc.model.token.MojangProfileToken;
import me.braydon.mc.model.token.MojangUsernameToUUIDToken;
import me.braydon.mc.repository.PlayerCacheRepository;
import me.braydon.mc.repository.PlayerNameCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

/**
 * A service for interacting with the Mojang API.
 *
 * @author Braydon
 */
@Service
@Log4j2(topic = "Mojang Service")
public final class MojangService {
    private static final String SESSION_SERVER_ENDPOINT = "https://sessionserver.mojang.com";
    private static final String API_ENDPOINT = "https://api.mojang.com";
    private static final String UUID_TO_PROFILE = SESSION_SERVER_ENDPOINT + "/session/minecraft/profile/%s";
    private static final String USERNAME_TO_UUID = API_ENDPOINT + "/users/profiles/minecraft/%s";

    /**
     * The cache repository for {@link Player}'s by their username.
     */
    @NonNull private final PlayerNameCacheRepository playerNameCache;

    /**
     * The cache repository for {@link Player}'s.
     */
    @NonNull private final PlayerCacheRepository playerCache;

    @Autowired
    public MojangService(@NonNull PlayerNameCacheRepository playerNameCache, @NonNull PlayerCacheRepository playerCache) {
        this.playerNameCache = playerNameCache;
        this.playerCache = playerCache;
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
     * @param query       the query to search for the player by
     * @param bypassCache should the cache be bypassed?
     * @return the player
     * @throws BadRequestException       if the UUID is malformed
     * @throws ResourceNotFoundException if the player is not found
     */
    @NonNull
    public CachedPlayer getPlayer(@NonNull String query, boolean bypassCache) throws BadRequestException, ResourceNotFoundException {
        log.info("Requesting player with query: {}", query);

        UUID uuid; // The player UUID to lookup
        boolean fullLength = query.length() == 36; // Was a UUID provided?
        if (query.length() == 32 || fullLength) { // Parse the query as a UUID
            try {
                uuid = fullLength ? UUID.fromString(query) : UUIDUtils.addDashes(query);
                log.info("Parsed {}UUID: {} -> {}", fullLength ? "" : "trimmed ", query, uuid);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Malformed UUID provided: %s".formatted(query));
            }
        } else { // The query is a username, request from Mojang
            uuid = usernameToUUID(query);
            log.info("Found UUID for username {}: {}", query, uuid);
        }

        // Check the cache for the player
        if (!bypassCache) {
            Optional<CachedPlayer> cached = playerCache.findById(uuid);
            if (cached.isPresent()) { // Respond with the cache if present
                log.info("Found player in cache: {}", uuid);
                return cached.get();
            }
        }

        // Send a request to Mojang requesting
        // the player profile by their UUID
        try {
            log.info("Retrieving player profile for UUID: {}", uuid);
            MojangProfileToken token = JsonWebRequest.makeRequest(
                    UUID_TO_PROFILE.formatted(uuid), HttpMethod.GET
            ).execute(MojangProfileToken.class);
            Tuple<Skin, Cape> skinAndCape = token.getSkinAndCape(); // Get the skin and cape
            ProfileAction[] profileActions = token.getProfileActions();

            // Build our player model, cache it, and then return it
            CachedPlayer player = new CachedPlayer(
                    uuid, token.getName(),
                    skinAndCape.getLeft() == null ? Skin.DEFAULT_STEVE : skinAndCape.getLeft(),
                    skinAndCape.getRight(),
                    profileActions.length == 0 ? null : profileActions,
                    System.currentTimeMillis()
            );
            if (!bypassCache) { // Store in the cache
                playerCache.save(player);
                log.info("Cached player: {}", uuid);
            }

            player.setCached(-1L); // Set to -1 to indicate it's not cached in the response
            return player;
        } catch (JsonWebException ex) {
            // No profile found, return null
            if (ex.getStatusCode() == 400) {
                throw new ResourceNotFoundException("Player not found with query: %s".formatted(query));
            }
            throw ex;
        }
    }

    /**
     * Resolve a Minecraft server on the given
     * platform with the given hostname.
     *
     * @param platformName the name of the platform
     * @param hostname the hostname of the server
     * @return the resolved Minecraft server
     * @throws InvalidMinecraftServerPlatform if the platform is invalid
     * @throws ResourceNotFoundException if the server isn't found
     */
    @NonNull
    public MinecraftServer getMinecraftServer(@NonNull String platformName, @NonNull String hostname)
            throws InvalidMinecraftServerPlatform, ResourceNotFoundException {
        MinecraftServer.Platform platform = EnumUtils.getEnumConstant(MinecraftServer.Platform.class, platformName.toUpperCase());
        if (platform == null) { // Invalid platform
            throw new InvalidMinecraftServerPlatform();
        }
        InetSocketAddress address = DNSUtils.resolveSRV(hostname); // Resolve the SRV record
        if (address == null) { // No address found
            throw new ResourceNotFoundException("No SRV record found for hostname: %s".formatted(hostname));
        }
        return platform.getPinger().ping(address.getHostName(), address.getPort()); // Ping the server and return with the response
    }

    /**
     * Get the UUID of a player by their username.
     *
     * @param username the player's username
     * @return the player's UUID
     * @throws ResourceNotFoundException if the player isn't found
     */
    @NonNull
    private UUID usernameToUUID(@NonNull String username) throws ResourceNotFoundException {
        username = username.toLowerCase(); // Lowercase the username

        // Check the cache for the player's UUID
        Optional<CachedPlayerName> cached = playerNameCache.findById(username);
        if (cached.isPresent()) { // Respond with the cache if present
            log.info("Found UUID in cache for username {}: {}", username, cached.get().getUniqueId());
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
            if (ex.getStatusCode() == 404) {
                throw new ResourceNotFoundException("Player not found with username: %s".formatted(username));
            }
            throw ex;
        }
    }
}