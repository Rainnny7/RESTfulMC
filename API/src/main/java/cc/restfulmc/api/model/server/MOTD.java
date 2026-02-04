package cc.restfulmc.api.model.server;

import cc.restfulmc.api.common.ColorUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;

/**
 * The MOTD for a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class MOTD {
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
     * TODO: improve this:
     *   - place into its own template file
     *   - add missing data: favicon, server name, player counts, and ping
     */
    @JsonIgnore
    public String generateHtmlPreview(@NonNull MinecraftServer server) {
        StringBuilder builder = new StringBuilder();
        for (String line : getHtml()) {
            builder.append(line).append("<br>");
        }
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%s</title>
                    <style>
                        @font-face {
                            font-family: "Minecraft";
                            src: url("https://cdn.fascinated.cc/minecraft-font.ttf") format("truetype");
                            font-weight: normal;
                            font-style: normal;
                        }
                        body {
                            margin: 0;
                            background-image: url("https://cdn.fascinated.cc/server_background.png");
                            background-repeat: repeat;
                            font-family: "Minecraft", system-ui, sans-serif;
                            font-size: 20px;
                            line-height: 1.4;
                        }
                    </style>
                </head>
                <body>
                %s
                </body>
                </html>
                """.formatted(
                server.getHostname(),
                builder.toString()
        );
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