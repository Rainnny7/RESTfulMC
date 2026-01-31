package cc.restfulmc.api.model.token.server;

import cc.restfulmc.api.model.server.java.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public abstract class GenericJavaServerStatusToken {
    /**
     * The description (MOTD) of this server.
     * <p>
     * Legacy: String, New: JSON Object
     * </p>
     */
    @NonNull private final Object description;

    /**
     * The player counts of this server.
     */
    @NonNull private final Players players;

    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * Player count data for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Players {
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
            @NonNull private final String name;
        }
    }
}