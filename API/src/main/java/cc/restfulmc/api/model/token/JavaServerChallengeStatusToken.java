package cc.restfulmc.api.model.token;

import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A token representing the response from
 * sending a challenge request via UDP to
 * a {@link JavaMinecraftServer} using the
 * query.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Getter @ToString
public final class JavaServerChallengeStatusToken {
    /**
     * The map (world) of this server.
     */
    @NonNull private final String map;

    /**
     * The software of this server.
     */
    @NonNull private final String software;

    /**
     * The plugins of this server.
     */
    private final Map<String, String> plugins;

    /**
     * Create a new challenge token
     * from the given raw data.
     *
     * @param rawData the raw data
     * @return the challenge token
     */
    @NonNull
    public static JavaServerChallengeStatusToken create(@NonNull Map<String, String> rawData) {
        String[] splitPlugins = rawData.get("plugins").split(": ");
        String software = splitPlugins[0]; // The server software

        Map<String, String> plugins = new HashMap<>();
        for (String plugin : splitPlugins[1].split("; ")) {
            String[] split = plugin.split(" ");
            plugins.put(split[0], split[1]);
        }
        return new JavaServerChallengeStatusToken(rawData.get("map"), software, plugins);
    }
}