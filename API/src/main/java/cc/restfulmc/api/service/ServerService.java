package cc.restfulmc.api.service;

import cc.restfulmc.api.common.*;
import cc.restfulmc.api.common.renderer.impl.server.ServerPreviewRenderer;
import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.dns.impl.ARecord;
import cc.restfulmc.api.model.dns.impl.SRVRecord;
import cc.restfulmc.api.model.server.AsnData;
import cc.restfulmc.api.model.server.GeoLocation;
import cc.restfulmc.api.model.server.MinecraftServer;
import cc.restfulmc.api.model.server.ServerPlatform;
import cc.restfulmc.api.model.server.cache.CachedMinecraftServer;
import cc.restfulmc.api.model.server.cache.CachedMinecraftServerMOTDTexture;
import cc.restfulmc.api.model.server.java.Favicon;
import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import cc.restfulmc.api.repository.MinecraftServerCacheRepository;
import cc.restfulmc.api.repository.MinecraftServerMOTDTextureCacheRepository;
import com.maxmind.geoip2.model.AsnResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author Braydon
 */
@Service @Log4j2(topic = "Server Service")
public final class ServerService {
    public static final String DEFAULT_SERVER_ICON = "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";

    private static final int MIN_MOTD_TEXTURE_SIZE = 64;
    private static final int MAX_MOTD_TEXTURE_SIZE = 1024;

    /**
     * The MaxMind service to use for Geo lookups.
     */
    @NonNull private final MaxMindService maxMindService;

    /**
     * The cache repository for {@link MinecraftServer}'s.
     */
    @NonNull private final MinecraftServerCacheRepository minecraftServerCache;

    /**
     * The cache repository for {@link MinecraftServer} MOTD's.
     */
    @NonNull private final MinecraftServerMOTDTextureCacheRepository motdTextureCacheRepository;

    /**
     * The Mojang service to use.
     */
    @NonNull private final MojangService mojangService;

    @Autowired
    public ServerService(@NonNull MaxMindService maxMindService, @NonNull MinecraftServerCacheRepository minecraftServerCache,
                         @NonNull MinecraftServerMOTDTextureCacheRepository motdTextureCacheRepository, @NonNull MojangService mojangService) {
        this.maxMindService = maxMindService;
        this.minecraftServerCache = minecraftServerCache;
        this.motdTextureCacheRepository = motdTextureCacheRepository;
        this.mojangService = mojangService;
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
        ServerPlatform platform = EnumUtils.getEnumConstant(ServerPlatform.class, platformName.toUpperCase());
        if (platform == null) { // Invalid platform
            throw new BadRequestException("Invalid platform: %s".formatted(platformName));
        }
        String lookupHostname = hostname; // The hostname used to lookup the server

        int port = platform.getDefaultPort(); // Port to ping
        if (hostname.contains(":")) { // Hostname contains a port
            String[] split = hostname.split(":");
            hostname = split[0];
            try { // Try and parse the port
                port = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) { // Invalid port
                throw new BadRequestException("Invalid port defined");
            }
        }
        String cacheKey = "%s-%s".formatted(platform.name(), lookupHostname.replace(":", "-"));

        // Check the cache for the server
        Optional<CachedMinecraftServer> cached = minecraftServerCache.findById(cacheKey);
        if (cached.isPresent()) { // Respond with the cache if present
            log.info("Found server in cache: {}", hostname);
            return cached.get();
        }
        List<DNSRecord> records = new ArrayList<>(); // The resolved DNS records for the server

        SRVRecord srvRecord = platform == ServerPlatform.JAVA ? DNSUtils.resolveSRV(hostname) : null; // Resolve the SRV record
        if (srvRecord != null) { // SRV was resolved, use the hostname and port
            records.add(srvRecord); // Going to need this for later
            InetSocketAddress socketAddress = srvRecord.getSocketAddress();
            hostname = socketAddress.getHostName();
            port = socketAddress.getPort();
        }

        ARecord aRecord = DNSUtils.resolveA(hostname); // Resolve the A record so we can get the IPv4 address
        String ip = aRecord == null ? null : aRecord.getAddress(); // Get the IP address
        if (ip != null) { // Was the IP resolved?
            records.add(aRecord); // Going to need this for later
            log.info("Resolved hostname: {} -> {}", hostname, ip);
        }
        // Attempt to perform a Geo lookup on the server
        AsnData asn = null;
        GeoLocation geo = null;
        try {
            log.info("Looking up ASN & Geo location data for {}...", ip);

            InetAddress address = InetAddress.getByName(ip == null ? hostname : ip);
            AsnResponse asnResponse = maxMindService.lookupAsn(address);
            if (asnResponse != null) {
                asn = new AsnData(asnResponse.autonomousSystemNumber(), asnResponse.autonomousSystemOrganization(), asnResponse.network().toString());
            }
            geo = maxMindService.lookup(address);
        } catch (Exception ex) {
            log.error("Failed looking up Geo location data for {}:", ip, ex);
        }

        // Build our server model, cache it, and then return it
        MinecraftServer response = platform.getPinger().ping(hostname, ip, port, records.toArray(new DNSRecord[0])); // Ping the server and await a response
        if (response == null) { // No response from ping
            throw new ResourceNotFoundException("Server didn't respond to ping");
        }
        // Update ASN & Geo location data in the server if present
        if (asn != null) {
            response.setAsn(asn);
        }
        if (geo != null) {
            response.setGeo(geo);
        }

        CachedMinecraftServer minecraftServer = new CachedMinecraftServer(
                cacheKey, response, System.currentTimeMillis()
        );
        // Get the blocked status of the Java server
        if (platform == ServerPlatform.JAVA) {
            ((JavaMinecraftServer) minecraftServer.getValue()).setMojangBanned(mojangService.isServerBlocked(hostname));
        }
        String finalHostname = hostname;
        CompletableFuture.runAsync(() -> {
            minecraftServerCache.save(minecraftServer);
            log.info("Cached server: {}", finalHostname);
        }, Constants.VIRTUAL_EXECUTOR);
        minecraftServer.setCached(-1L); // Set to -1 to indicate it's not cached in the response
        return minecraftServer;
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
            Favicon favicon = ((JavaMinecraftServer) getMinecraftServer(ServerPlatform.JAVA.name(), hostname).getValue()).getFavicon();
            if (favicon != null) { // Use the server's favicon
                icon = favicon.getBase64();
                icon = icon.substring(icon.indexOf(",") + 1); // Remove the data type from the server icon
            }
        } catch (BadRequestException | ResourceNotFoundException ignored) {
            // Safely ignore these, we will use the default server icon
        }
        try {
            assert icon != null;
            return Base64.getDecoder().decode(icon); // Return the decoded favicon
        } catch (Exception ex) { // Use the default server icon
            log.error("Failed getting server favicon for {}:", hostname, ex);
            return Base64.getDecoder().decode(DEFAULT_SERVER_ICON);
        }
    }

    /**
     * Get the MOTD of the server on the given
     * platform and with the given hostname.
     *
     * @param platformName the platform of the server
     * @param hostname the hostname of the server
     * @param size the optional size of the MOTD
     * @return the server MOTD
     */
    public byte[] getServerMotd(@NonNull String platformName, @NonNull String hostname, int size) throws BadRequestException, ResourceNotFoundException {
        // Ensure the size is within bounds
        if (size < MIN_MOTD_TEXTURE_SIZE || size > MAX_MOTD_TEXTURE_SIZE) {
            throw new BadRequestException("Invalid server MOTD size. Must be between " + MIN_MOTD_TEXTURE_SIZE + " and " + MAX_MOTD_TEXTURE_SIZE);
        }
        MinecraftServer server = getMinecraftServer(platformName, hostname).getValue();
        ServerPlatform platform = Objects.requireNonNull(EnumUtils.getEnumConstant(ServerPlatform.class, platformName.toUpperCase()));
        String cacheKey = "%s-%s-%s-%s".formatted(platform.name(), server.getHostname(), server.getPort(), size);
        log.info("Getting MOTD for server: {}:{} (size {})", server.getHostname(), server.getPort(), size);
        long before = System.currentTimeMillis();

        // In production environments, first try the cache and return that if present
        CachedMinecraftServerMOTDTexture cachedMotdTexture = EnvironmentUtils.isProduction() ? motdTextureCacheRepository.findById(cacheKey).orElse(null) : null;
        if (cachedMotdTexture != null) {
            log.info("Found server MOTD {}:{} from cache in {}ms", server.getHostname(), server.getPort(), System.currentTimeMillis() - before);
            return cachedMotdTexture.getTexture();
        }
        // Render the server MOTD and cache it
        byte[] motd = ImageUtils.toByteArray(ServerPreviewRenderer.INSTANCE.render(server, size));
        if (EnvironmentUtils.isProduction()) {
            CompletableFuture.runAsync(() -> {
                motdTextureCacheRepository.save(new CachedMinecraftServerMOTDTexture(cacheKey, motd));
            }, Constants.VIRTUAL_EXECUTOR);
        }
        return motd;
    }
}