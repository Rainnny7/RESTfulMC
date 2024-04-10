/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.common.*;
import me.braydon.mc.common.web.JsonWebException;
import me.braydon.mc.common.web.JsonWebRequest;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.MojangRateLimitException;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.*;
import me.braydon.mc.model.cache.CachedMinecraftServer;
import me.braydon.mc.model.cache.CachedPlayer;
import me.braydon.mc.model.cache.CachedPlayerName;
import me.braydon.mc.model.server.JavaMinecraftServer;
import me.braydon.mc.model.token.MojangProfileToken;
import me.braydon.mc.model.token.MojangUsernameToUUIDToken;
import me.braydon.mc.repository.MinecraftServerCacheRepository;
import me.braydon.mc.repository.PlayerCacheRepository;
import me.braydon.mc.repository.PlayerNameCacheRepository;
import net.jodah.expiringmap.ExpirationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private static final String FETCH_BLOCKED_SERVERS = SESSION_SERVER_ENDPOINT + "/blockedservers";

    private static final int DEFAULT_PART_TEXTURE_SIZE = 128;
    private static final int MAX_PART_TEXTURE_SIZE = 512;

    private static final String DEFAULT_SERVER_ICON = "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";

    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    private static final Joiner DOT_JOINER = Joiner.on('.');

    /**
     * The cache repository for {@link Player}'s by their username.
     */
    @NonNull private final PlayerNameCacheRepository playerNameCache;

    /**
     * The cache repository for {@link Player}'s.
     */
    @NonNull private final PlayerCacheRepository playerCache;

    /**
     * The cache repository for {@link MinecraftServer}'s.
     */
    @NonNull private final MinecraftServerCacheRepository minecraftServerCache;

    /**
     * A list of banned server hashes provided by Mojang.
     * <p>
     * This is periodically fetched from Mojang, see
     * {@link #fetchBlockedServers()} for more info.
     * </p>
     *
     * @see <a href="https://wiki.vg/Mojang_API#Blocked_Servers">Mojang API</a>
     */
    private List<String> bannedServerHashes;

    /**
     * A cache of blocked server hostnames.
     *
     * @see #isServerHostnameBlocked(String) for more
     */
    private final ExpiringSet<String> blockedServersCache = new ExpiringSet<>(ExpirationPolicy.CREATED, 10L, TimeUnit.MINUTES);

    @Autowired
    public MojangService(@NonNull PlayerNameCacheRepository playerNameCache, @NonNull PlayerCacheRepository playerCache,
                         @NonNull MinecraftServerCacheRepository minecraftServerCache) {
        this.playerNameCache = playerNameCache;
        this.playerCache = playerCache;
        this.minecraftServerCache = minecraftServerCache;
    }

    @PostConstruct
    public void onInitialize() {
        // Schedule a task to fetch blocked
        // servers from Mojang every 15 minutes.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchBlockedServers();
            }
        }, 0L, 60L * 15L * 1000L);
    }

    /**
     * Get the part of a skin texture for
     * a player by their username or UUID.
     *
     * @param partName   the part of the player's skin texture to get
     * @param query      the query to search for the player by
     * @param extension  the skin part image extension
     * @param sizeString the size of the skin part image
     * @return the skin part texture
     * @throws BadRequestException      if the extension is invalid
     * @throws MojangRateLimitException if the Mojang API rate limit is reached
     */
    public byte[] getSkinPartTexture(@NonNull String partName, @NonNull String query, @NonNull String extension, String sizeString)
            throws BadRequestException, MojangRateLimitException {
        Integer size = null;
        if (sizeString != null) { // Attempt to parse the size
            try {
                size = Integer.parseInt(sizeString);
            } catch (NumberFormatException ignored) {
                // Safely ignore, invalid number provided
            }
        }
        Skin.Part part = EnumUtils.getEnumConstant(Skin.Part.class, partName.toUpperCase()); // The skin part to get
        if (part == null) { // Default to the head part
            part = Skin.Part.HEAD;
        }
        if (extension.isBlank()) { // Invalid extension
            throw new BadRequestException("Invalid extension");
        }
        if (size == null || size <= 0) { // Invalid size
            size = DEFAULT_PART_TEXTURE_SIZE;
        }
        size = Math.min(size, MAX_PART_TEXTURE_SIZE); // Limit the size to 512

        Skin target = null; // The target skin to get the skin part of
        try {
            CachedPlayer player = getPlayer(query, false); // Retrieve the player
            target = player.getSkin(); // Use the player's skin
        } catch (Exception ignored) {
            // Simply ignore, and fallback to the default skin
        }
        if (target == null) { // Fallback to the default skin
            target = Skin.DEFAULT_STEVE;
        }
        return ImageUtils.getSkinPart(target, part, size);
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
     * @throws BadRequestException       if the UUID or username is invalid
     * @throws ResourceNotFoundException if the player is not found
     * @throws MojangRateLimitException  if the Mojang API rate limit is reached
     */
    @NonNull
    public CachedPlayer getPlayer(@NonNull String query, boolean bypassCache)
            throws BadRequestException, ResourceNotFoundException, MojangRateLimitException {
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
     * Get the favicon of a Java
     * server with the given hostname.
     * <p>
     * If the favicon of the server cannot be
     * retrieved, the default icon will be used.
     * </p>
     *
     * @param hostname the hostname of the server
     * @return the server favicon
     * @see #DEFAULT_SERVER_ICON for the default server icon
     */
    public byte[] getServerFavicon(@NonNull String hostname) {
        String icon = null; // The server base64 icon
        try {
            JavaMinecraftServer.Favicon favicon = ((JavaMinecraftServer) getMinecraftServer(MinecraftServer.Platform.JAVA.name(), hostname).getValue()).getFavicon();
            if (favicon != null) { // Use the server's favicon
                icon = favicon.getBase64();
                icon = icon.substring(icon.indexOf(",") + 1); // Remove the data type from the server icon
            }
        } catch (BadRequestException | ResourceNotFoundException ignored) {
            // Safely ignore these, we will use the default server icon
        }
        if (icon == null) { // Use the default server icon
            icon = DEFAULT_SERVER_ICON;
        }
        return Base64.getDecoder().decode(icon); // Return the decoded favicon
    }

    /**
     * Check if the server with the
     * given hostname is blocked by Mojang.
     *
     * @param hostname the server hostname to check
     * @return whether the hostname is blocked
     */
    public boolean isServerBlocked(@NonNull String hostname) {
        // Remove trailing dots
        while (hostname.charAt(hostname.length() - 1) == '.') {
            hostname = hostname.substring(0, hostname.length() - 1);
        }
        // Is the hostname banned?
        if (isServerHostnameBlocked(hostname)) {
            return true;
        }
        List<String> splitDots = Lists.newArrayList(DOT_SPLITTER.split(hostname)); // Split the hostname by dots
        boolean isIp = splitDots.size() == 4; // Is it an IP address?
        if (isIp) {
            for (String element : splitDots) {
                try {
                    int part = Integer.parseInt(element);
                    if (part >= 0 && part <= 255) { // Ensure the part is within the valid range
                        continue;
                    }
                } catch (NumberFormatException ignored) {
                    // Safely ignore, not a number
                }
                isIp = false;
                break;
            }
        }
        // Check if the hostname is blocked
        if (!isIp && isServerHostnameBlocked("*." + hostname)) {
            return true;
        }
        // Additional checks for the hostname
        while (splitDots.size() > 1) {
            splitDots.remove(isIp ? splitDots.size() - 1 : 0);
            String starredPart = isIp ? DOT_JOINER.join(splitDots) + ".*" : "*." + DOT_JOINER.join(splitDots);
            if (isServerHostnameBlocked(starredPart)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolve a Minecraft server on the given
     * platform with the given hostname.
     *
     * @param platformName the name of the platform
     * @param hostname     the hostname of the server
     * @return the resolved Minecraft server
     * @throws BadRequestException       if the hostname, platform, or port is invalid
     * @throws ResourceNotFoundException if the server isn't found
     */
    @NonNull
    public CachedMinecraftServer getMinecraftServer(@NonNull String platformName, @NonNull String hostname)
            throws BadRequestException, ResourceNotFoundException {
        MinecraftServer.Platform platform = EnumUtils.getEnumConstant(MinecraftServer.Platform.class, platformName.toUpperCase());
        if (platform == null) { // Invalid platform
            throw new BadRequestException("Invalid platform: %s".formatted(platformName));
        }
        String lookupHostname = hostname; // The hostname used to lookup the server

        int port = platform.getDefaultPort(); // Port to ping
        String portString = null;
        if (hostname.contains(":")) { // Hostname contains a port
            String[] split = hostname.split(":");
            hostname = split[0];
            portString = split[1];
            try { // Try and parse the port
                port = Integer.parseInt(portString);
            } catch (NumberFormatException ex) { // Invalid port
                throw new BadRequestException("Invalid port defined");
            }
        }

        // Check the cache for the server
        Optional<CachedMinecraftServer> cached = minecraftServerCache.findById(platform.name() + "-" + hostname + "-" + port);
        if (cached.isPresent()) { // Respond with the cache if present
            log.info("Found server in cache: {}", hostname);
            return cached.get();
        }
        InetSocketAddress address = platform == MinecraftServer.Platform.JAVA ? DNSUtils.resolveSRV(hostname) : null; // Resolve the SRV record
        if (address != null) { // SRV was resolved, use the hostname and port
            hostname = address.getHostName();
            port = address.getPort();
        }

        // Build our server model, cache it, and then return it
        CachedMinecraftServer minecraftServer = new CachedMinecraftServer(
                platform.name() + "-" + lookupHostname + "-" + (portString == null ? port : portString),
                platform.getPinger().ping(hostname, port),
                System.currentTimeMillis()
        );

        // Get the blocked status of the Java server
        if (platform == MinecraftServer.Platform.JAVA) {
            ((JavaMinecraftServer) minecraftServer.getValue()).setMojangBanned(isServerBlocked(hostname));
        }

        minecraftServerCache.save(minecraftServer);
        log.info("Cached server: {}", hostname);
        minecraftServer.setCached(-1L); // Set to -1 to indicate it's not cached in the response
        return minecraftServer;
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

    /**
     * Fetch a list of blocked servers from Mojang.
     */
    @SneakyThrows
    private void fetchBlockedServers() {
        try (
                InputStream inputStream = new URL(FETCH_BLOCKED_SERVERS).openStream();
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\n");
        ) {
            List<String> hashes = new ArrayList<>();
            while (scanner.hasNext()) {
                hashes.add(scanner.next());
            }
            bannedServerHashes = Collections.synchronizedList(hashes);
            log.info("Fetched {} banned server hashes", bannedServerHashes.size());
        }
    }

    /**
     * Check if the hash for the given
     * hostname is in the blocked server list.
     *
     * @param hostname the hostname to check
     * @return whether the hostname is blocked
     */
    private boolean isServerHostnameBlocked(@NonNull String hostname) {
        // Check the cache first for the hostname
        if (blockedServersCache.contains(hostname)) {
            return true;
        }
        String hashed = Hashing.sha1().hashBytes(hostname.toLowerCase().getBytes(StandardCharsets.ISO_8859_1)).toString();
        boolean blocked = bannedServerHashes.contains(hashed); // Is the hostname blocked?
        if (blocked) { // Cache the blocked hostname
            blockedServersCache.add(hostname);
        }
        return blocked;
    }
}