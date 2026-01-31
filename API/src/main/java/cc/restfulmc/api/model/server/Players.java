package cc.restfulmc.api.model.server;

import cc.restfulmc.api.common.ColorUtils;
import cc.restfulmc.api.config.AppConfig;
import cc.restfulmc.api.model.token.JavaServerStatusToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Player count data for a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class Players {
    /**
     * The online players on this server.
     */
    private final int online;

    /**
     * The maximum allowed players on this server.
     */
    private final int max;

    /**
     * A sample of players on this server, null or empty if no sample.
     */
    private final Sample[] sample;

    /**
     * Create new player count data from a token.
     *
     * @param token the token to create from
     * @return the player count data
     */
    @NonNull
    public static Players create(@NonNull JavaServerStatusToken.Players token) {
        List<Players.Sample> samples = null;
        if (token.getSample() != null) {
            samples = new ArrayList<>(); // The player samples
            for (JavaServerStatusToken.Players.Sample sample : token.getSample()) {
                String href = AppConfig.INSTANCE.getServerPublicUrl() + "/player/" + sample.getId();
                samples.add(new Players.Sample(sample.getId(), Players.Sample.Name.create(sample.getName()), href));
            }
        }
        return new Players(token.getOnline(), token.getMax(), samples != null ? samples.toArray(new Players.Sample[0]) : null);
    }

    /**
     * A sample player.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Sample {
        /**
         * The unique id of this player.
         */
        @NonNull private final UUID id;

        /**
         * The name of this player.
         */
        @NonNull private final Players.Sample.Name name;

        /**
         * The href to view player data for this player sample.
         */
        @NonNull private final String url;

        /**
         * The name of a sample player.
         */
        @AllArgsConstructor @Getter @ToString
        public static class Name {
            /**
             * The raw name.
             */
            @NonNull private final String raw;

            /**
             * The clean name (no color codes).
             */
            @NonNull private final String clean;

            /**
             * The HTML name.
             */
            @NonNull private final String html;

            /**
             * Create a new name from a raw string.
             *
             * @param raw the raw name string
             * @return the new name
             */
            @NonNull
            public static Players.Sample.Name create(@NonNull String raw) {
                return new Players.Sample.Name(raw, ColorUtils.stripColor(raw), ColorUtils.toHTML(raw));
            }
        }
    }
}