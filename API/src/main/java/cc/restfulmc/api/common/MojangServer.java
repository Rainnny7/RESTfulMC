package cc.restfulmc.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents a service provided by Mojang.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public enum MojangServer {
    SESSION("Session Server", "https://sessionserver.mojang.com"),
    API("Mojang API", "https://api.mojang.com"),
    TEXTURES("Textures Server", "https://textures.minecraft.net"),
    ASSETS("Assets Server", "https://assets.mojang.com"),
    LIBRARIES("Libraries Server", "https://libraries.minecraft.net"),
    SERVICES("Minecraft Services", "https://api.minecraftservices.com");

    private static final int STATUS_TIMEOUT = 7000;

    /**
     * The name of this server.
     */
    @NonNull private final String name;

    /**
     * The endpoint of this service.
     */
    @NonNull private final String endpoint;

    /**
     * Ping this service and get the status of it.
     *
     * @return the service status
     */
    @NonNull
    public Status getStatus() {
        try {
            InetAddress address = InetAddress.getByName(endpoint.substring(8));
            long before = System.currentTimeMillis();
            if (address.isReachable(STATUS_TIMEOUT)) {
                // The time it took to reach the host is 75% of
                // the timeout, consider it to be degraded.
                if ((System.currentTimeMillis() - before) > STATUS_TIMEOUT * 0.75D) {
                    return Status.DEGRADED;
                }
                return Status.ONLINE;
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ignored) {
            // We can safely ignore any errors, we're simply checking
            // if the host is reachable, if it's not, it's offline.
        }
        return Status.OFFLINE;
    }

    /**
     * The status of a service.
     */
    public enum Status {
        /**
         * The service is online and accessible.
         */
        ONLINE,

        /**
         * The service is online, but is experiencing degraded performance.
         */
        DEGRADED,

        /**
         * The service is offline and inaccessible.
         */
        OFFLINE
    }
}