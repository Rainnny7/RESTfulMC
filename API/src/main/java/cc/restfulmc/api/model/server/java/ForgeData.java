package cc.restfulmc.api.model.server.java;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Forge information for a server.
 * <p>
 * This is for servers on 1.13 and above.
 * </p>
 * 
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class ForgeData {
    /**
     * The list of channels on this server, null or empty if none.
     */
    private final Channel[] channels;

    /**
     * The list of mods on this server, null or empty if none.
     */
    private final Mod[] mods;

    /**
     * The version of the FML network.
     */
    private final int fmlNetworkVersion;

    /**
     * Are the channel and mod lists truncated?
     * <p>
     * Legacy versions see truncated lists, modern
     * versions ignore this truncated flag.
     * </p>
     */
    private final boolean truncated;

    /**
     * A Forge channel for a server.
     */
    @AllArgsConstructor @Getter @ToString
    private static class Channel {
        /**
         * The name of this channel.
         */
        @NonNull @SerializedName("res") private final String name;

        /**
         * The version of this channel.
         */
        @NonNull private final String version;

        /**
         * Whether this channel is required.
         */
        private final boolean required;
    }

    /**
     * A Forge mod for a server.
     */
    @AllArgsConstructor @Getter @ToString
    private static class Mod {
        /**
         * The id of this mod.
         */
        @NonNull @SerializedName("modId") private final String name;

        /**
         * The marker for this mod.
         */
        @NonNull @SerializedName("modmarker") private final String marker;
    }
}