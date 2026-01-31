package cc.restfulmc.api.model.server;

import cc.restfulmc.api.common.ColorUtils;
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