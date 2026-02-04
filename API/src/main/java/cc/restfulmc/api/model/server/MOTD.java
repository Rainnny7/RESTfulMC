package cc.restfulmc.api.model.server;

import cc.restfulmc.api.RESTfulMC;
import cc.restfulmc.api.common.ColorUtils;
import cc.restfulmc.api.common.Constants;
import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import cc.restfulmc.api.service.ServerService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * The MOTD for a server.
 *
 * @author Braydon
 */
@Log4j2 @AllArgsConstructor @Getter @ToString
public final class MOTD {
    /**
     * The HTML preview template.
     */
    private static String HTML_TEMPLATE;
    static {
        try (InputStream inputStream = RESTfulMC.class.getResourceAsStream("/templates/motd-preview.html")) {
            if (inputStream != null) {
                HTML_TEMPLATE = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException ex) {
            log.error("Failed to load MOTD preview template", ex);
        }
    }

    /**
     * The raw MOTD lines.
     */
    @NonNull private final String[] raw;

    /**
     * The clean MOTD lines (no color codes).
     */
    @NonNull private final String[] clean;

    /**
     * The HTML MOTD lines.
     */
    @NonNull private final String[] html;

    /**
     * Generates an HTML representation for the MOTD.
     *
     * @param server the server to generate the HTML for
     * @return the generated HTML
     */
    @JsonIgnore
    public String generateHtmlPreview(@NonNull MinecraftServer server) {
        // Build MOTD HTML lines
        StringBuilder motdBuilder = new StringBuilder();
        for (String line : getHtml()) {
            motdBuilder.append("<div class=\"motd-line\">").append(line).append("</div>");
        }

        // Get the favicon (use default if not available or not a Java server)
        String faviconData;
        if (server instanceof JavaMinecraftServer javaServer && javaServer.getFavicon() != null) {
            faviconData = javaServer.getFavicon().getBase64();
        } else {
            faviconData = ServerService.DEFAULT_SERVER_ICON;
        }
        // Replace template placeholders
        return HTML_TEMPLATE
                .replace("{{cdn}}", Constants.CDN_URL)
                .replace("{{hostname}}", server.getHostname())
                .replace("{{favicon}}", faviconData)
                .replace("{{playersOnline}}", String.valueOf(server.getPlayers().getOnline()))
                .replace("{{playersMax}}", String.valueOf(server.getPlayers().getMax()))
                .replace("{{motd}}", motdBuilder.toString());
    }

    /**
     * Create a new MOTD from a raw string.
     *
     * @param raw the raw motd string
     * @return the new motd
     */
    @NonNull
    public static MOTD create(@NonNull String raw) {
        String[] rawLines = raw.split("\n"); // The raw lines
        return new MOTD(
                rawLines,
                Arrays.stream(rawLines).map(ColorUtils::stripColor).toArray(String[]::new),
                Arrays.stream(rawLines).map(ColorUtils::toHTML).toArray(String[]::new)
        );
    }
}