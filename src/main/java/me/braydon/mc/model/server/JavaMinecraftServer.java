package me.braydon.mc.model.server;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.braydon.mc.RESTfulMC;
import me.braydon.mc.common.MinecraftVersion;
import me.braydon.mc.model.MinecraftServer;
import me.braydon.mc.model.token.JavaServerStatusToken;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * A Java edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@Getter
public final class JavaMinecraftServer extends MinecraftServer {
    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The Forge mod information for this server, null if none.
     */
    private final ModInfo modInfo;

    /**
     * Does this server enforce secure chat?
     */
    private final boolean enforcesSecureChat;

    /**
     * Is this server preventing chat reports?
     */
    private final boolean preventsChatReports;

    /**
     * Is this server on the list
     * of blocked servers by Mojang?
     *
     * @see <a href="https://wiki.vg/Mojang_API#Blocked_Servers">Mojang API</a>
     */
    private final boolean mojangBanned;

    private JavaMinecraftServer(@NonNull String hostname, String ip, int port, @NonNull Players players,
                                @NonNull MOTD motd, String icon, @NonNull Version version, ModInfo modInfo,
                                boolean enforcesSecureChat, boolean preventsChatReports, boolean mojangBanned) {
        super(hostname, ip, port, players, motd, icon);
        this.version = version;
        this.modInfo = modInfo;
        this.enforcesSecureChat = enforcesSecureChat;
        this.preventsChatReports = preventsChatReports;
        this.mojangBanned = mojangBanned;
    }

    /**
     * Create a new Java Minecraft server.
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param token the status token
     * @return the Java Minecraft server
     */
    @NonNull
    public static JavaMinecraftServer create(@NonNull String hostname, String ip, int port, @NonNull JavaServerStatusToken token) {
        String motdString = token.getDescription() instanceof String ? (String) token.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = new TextComponent(ComponentSerializer.parse(RESTfulMC.GSON.toJson(token.getDescription()))).toLegacyText();
        }
        return new JavaMinecraftServer(hostname, ip, port, token.getPlayers(), MOTD.create(motdString),
                token.getFavicon(), token.getVersion().detailedCopy(), token.getModInfo(),
                token.isEnforcesSecureChat(), token.isPreventsChatReports(), false
        );
    }

    /**
     * Version information for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Version {
        /**
         * The version name of the server.
         */
        @NonNull private final String name;

        /**
         * The identified platform of the server, null if unknown.
         */
        private String platform;

        /**
         * The protocol version of the server.
         */
        private final int protocol;

        /**
         * The name of the version for the protocol, null if unknown.
         */
        private final String protocolName;

        /**
         * Create a more detailed
         * copy of this object.
         *
         * @return the detailed copy
         */
        @NonNull
        public Version detailedCopy() {
            String platform = null;
            if (name.contains(" ")) { // Parse the server platform
                String[] split = name.split(" ");
                if (split.length == 2) {
                    platform = split[0];
                }
            }
            MinecraftVersion minecraftVersion = MinecraftVersion.byProtocol(protocol);
            return new Version(name, platform, protocol, minecraftVersion == null ? null : minecraftVersion.getName());
        }
    }

    /**
     * Forge mod information for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class ModInfo {
        /**
         * The type of modded server this is.
         */
        @NonNull private final String type;

        /**
         * The list of mods on this server, null or empty if none.
         */
        private final ForgeMod[] modList;
    }

    @AllArgsConstructor @Getter @ToString
    private static class ForgeMod {
        /**
         * The id of this mod.
         */
        @NonNull @SerializedName("modid") private final String id;

        /**
         * The version of this mod.
         */
        private final String version;
    }
}