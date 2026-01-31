package cc.restfulmc.api.model.token;

import cc.restfulmc.api.model.server.java.ForgeData;
import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import cc.restfulmc.api.model.server.java.ModInfo;
import cc.restfulmc.api.model.server.java.Version;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

/**
 * A token representing the response from
 * pinging a {@link JavaMinecraftServer}.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class JavaServerStatusToken {
    /**
     * The description (MOTD) of this server.
     * <p>
     * Legacy: String, New: JSON Object
     * </p>
     */
    @NonNull private final Object description;

    /**
     * The base64 encoded favicon of this server, null if no favicon.
     */
    private final String favicon;

    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The player counts of this server.
     */
    @NonNull private final Players players;

    /**
     * The Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.12 or below.
     * </p>
     */
    @SerializedName("modinfo") private final ModInfo modInfo;

    /**
     * The Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.13 and above.
     * </p>
     */
    private final ForgeData forgeData;

    /**
     * Does this server preview chat?
     *
     * @see <a href="https://www.minecraft.net/es-mx/article/minecraft-snapshot-22w19a">This for more</a>
     */
    private final boolean previewsChat;

    /**
     * Does this server enforce secure chat?
     */
    private final boolean enforcesSecureChat;

    /**
     * Is this server preventing chat reports?
     */
    private final boolean preventsChatReports;

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