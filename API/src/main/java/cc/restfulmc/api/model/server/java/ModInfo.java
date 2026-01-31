package cc.restfulmc.api.model.server.java;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Forge mod information for a server.
 * <p>
 * This is for servers on 1.12 or below.
 * </p>
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class ModInfo {
    /**
     * The type of modded server this is.
     */
    @NonNull private final String type;

    /**
     * The list of mods on this server, null or empty if none.
     */
    @SerializedName("modList") private final Mod[] mods;

    /**
     * A Forge mod for a server.
     */
    @AllArgsConstructor @Getter @ToString
    private static class Mod {
        /**
         * The name of this mod.
         */
        @NonNull @SerializedName("modid") private final String name;

        /**
         * The version of this mod.
         */
        private final String version;
    }
}