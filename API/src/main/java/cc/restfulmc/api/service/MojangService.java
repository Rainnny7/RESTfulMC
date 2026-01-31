package cc.restfulmc.api.service;

import cc.restfulmc.api.common.ExpiringSet;
import cc.restfulmc.api.common.MojangServer;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import cc.restfulmc.api.common.ExpiringSet.ExpirationPolicy;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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
    private static final String FETCH_BLOCKED_SERVERS = MojangServer.SESSION.getEndpoint() + "/blockedservers";

    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    private static final Joiner DOT_JOINER = Joiner.on('.');

    /**
     * Mapped statuses for {@link MojangServer}'s.
     */
    @Getter private final Map<MojangServer, MojangServer.Status> mojangServerStatuses = Collections.synchronizedMap(new HashMap<>());

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

    @PostConstruct
    public void onInitialize() {
        // Schedule a task to fetch statuses
        // of Mojang servers every few minutes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchMojangServerStatuses();
            }
        }, 0L, 60L * 3L * 1000L);

        // Schedule a task to fetch blocked
        // servers from Mojang every hour.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchBlockedServers();
            }
        }, 0L, 60L * 60L * 1000L);
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
     * Fetch the statuses of {@link MojangServer}'s.
     */
    @SneakyThrows
    private void fetchMojangServerStatuses() {
        log.info("Checking Mojang server statuses...");
        Arrays.stream(MojangServer.values()).parallel().forEach(server -> {
            log.info("Pinging {}...", server.getEndpoint());
            MojangServer.Status status = server.getStatus(); // Retrieve the server status
            log.info("Retrieved status of {}: {}", server.getEndpoint(), status.name());
            mojangServerStatuses.put(server, status); // Cache the server status
        });
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
        boolean blocked = bannedServerHashes != null && (bannedServerHashes.contains(hashed)); // Is the hostname blocked?
        if (blocked) { // Cache the blocked hostname
            blockedServersCache.add(hostname);
        }
        return blocked;
    }

    /**
     * Cleanup when the app is destroyed.
     */
    @PreDestroy
    public void cleanup() {
        mojangServerStatuses.clear();
        bannedServerHashes.clear();
        blockedServersCache.clear();
    }
}