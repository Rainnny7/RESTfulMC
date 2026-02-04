package cc.restfulmc.api.model.server.java;

import cc.restfulmc.api.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The favicon for a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class Favicon {
    /**
     * The raw Base64 encoded favicon.
     */
    @NonNull private final String base64;

    /**
     * The URL to the favicon.
     */
    @NonNull private final String url;

    /**
     * Create a new favicon for a server.
     *
     * @param base64 the Base64 encoded favicon
     * @param hostname the server hostname
     * @return the favicon, null if none
     */
    public static Favicon create(String base64, @NonNull String hostname) {
        if (base64 == null) { // No favicon to create
            return null;
        }
        return new Favicon(
                base64,
                AppConfig.INSTANCE.getServerPublicUrl() + "/server/" + hostname + "/icon.png"
        );
    }
}