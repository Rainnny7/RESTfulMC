package cc.restfulmc.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .timeout(Constants.HTTP_CLIENT_TIMEOUT)
                    .GET()
                    .build();
            long before = System.currentTimeMillis();
            Constants.HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());

            // The time it took to reach the host is 75% of
            // the timeout, consider it to be degraded.
            if ((System.currentTimeMillis() - before) > Constants.HTTP_CLIENT_TIMEOUT.toMillis() * 0.75D) {
                return Status.DEGRADED;
            }
            return Status.ONLINE;
        } catch (Exception ignored) {
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