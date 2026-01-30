package cc.restfulmc.api.model.server;

import cc.restfulmc.api.common.JavaMinecraftVersion;
import cc.restfulmc.api.config.AppConfig;
import cc.restfulmc.api.model.MinecraftServer;
import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.token.JavaServerChallengeStatusToken;
import cc.restfulmc.api.model.token.JavaServerStatusToken;
import cc.restfulmc.api.service.MojangService;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Java edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@Setter @Getter @ToString(callSuper = true)
public final class JavaMinecraftServer extends MinecraftServer {
    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The favicon of this server, null if none.
     */
    private final Favicon favicon;

    /**
     * The software of this server, present if query is on.
     */
    private final String software;

    /**
     * The plugins on this server, present if
     * query is on and plugins are present.
     */
    private final Plugin[] plugins;

    /**
     * The Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.12 or below.
     * </p>
     */
    private final ModInfo modInfo;

    /**
     * The Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.13 and above.
     * </p>
     */
    private final ForgeData forgeData;

    /**
     * The main world of this server, present if query is on.
     */
    private final String world;

    /**
     * Does this server support querying?
     */
    private final boolean queryEnabled;

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
     * Is this server on the list
     * of blocked servers by Mojang?
     * <p>
     * This value is later set by the
     * {@link MojangService} when a server
     * is requested.
     * </p>
     *
     * @see <a href="https://wiki.vg/Mojang_API#Blocked_Servers">Mojang API</a>
     */
    private boolean mojangBanned;

    private JavaMinecraftServer(@NonNull String hostname, String ip, int port, DNSRecord[] records, GeoLocation geo,
                                @NonNull Version version, @NonNull Players players, @NonNull MOTD motd, Favicon favicon,
                                String software, Plugin[] plugins, ModInfo modInfo, ForgeData forgeData, String world,
                                boolean queryEnabled, boolean previewsChat, boolean enforcesSecureChat, boolean preventsChatReports,
                                boolean mojangBanned) {
        super(hostname, ip, port, records, geo, players, motd);
        this.version = version;
        this.favicon = favicon;
        this.software = software;
        this.plugins = plugins;
        this.modInfo = modInfo;
        this.forgeData = forgeData;
        this.world = world;
        this.queryEnabled = queryEnabled;
        this.previewsChat = previewsChat;
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
     * @param records the DNS records of the server, if any
     * @param statusToken the status token
     * @param challengeStatusToken the challenge status token, null if none
     * @return the Java Minecraft server
     */
    @NonNull
    public static JavaMinecraftServer create(@NonNull String hostname, String ip, int port, DNSRecord[] records,
                                             @NonNull JavaServerStatusToken statusToken, JavaServerChallengeStatusToken challengeStatusToken) {
        String motdString = statusToken.getDescription() instanceof String ? (String) statusToken.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = new TextComponent(ComponentSerializer.parse(AppConfig.GSON.toJson(statusToken.getDescription()))).toLegacyText();
        }
        String software = challengeStatusToken == null ? null : challengeStatusToken.getSoftware(); // The server software

        // Get the plugins from the challenge token
        Plugin[] plugins = null;
        if (challengeStatusToken != null) {
            List<Plugin> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : challengeStatusToken.getPlugins().entrySet()) {
                list.add(new Plugin(entry.getKey(), entry.getValue()));
            }
            plugins = list.toArray(new Plugin[0]);
        }
        String world = challengeStatusToken == null ? null : challengeStatusToken.getMap(); // The main server world

        return new JavaMinecraftServer(hostname, ip, port, records, null, statusToken.getVersion().detailedCopy(), Players.create(statusToken.getPlayers()),
                MOTD.create(motdString), Favicon.create(statusToken.getFavicon(), hostname), software, plugins, statusToken.getModInfo(),
                statusToken.getForgeData(), world, challengeStatusToken != null, statusToken.isPreviewsChat(),
                statusToken.isEnforcesSecureChat(), statusToken.isPreventsChatReports(), false
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
         * A list of versions supported by this server.
         */
        private final int[] supportedVersions;

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
            JavaMinecraftVersion minecraftVersion = JavaMinecraftVersion.byProtocol(protocol);
            if (minecraftVersion == JavaMinecraftVersion.UNKNOWN) {
                minecraftVersion = null;
            }
            return new Version(name, platform, protocol, supportedVersions, minecraftVersion == null ? null : minecraftVersion.getName());
        }
    }

    /**
     * The favicon for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Favicon {
        /**
         * The raw Base64 encoded favicon.
         */
        @NonNull private final String base64;

        /**
         * The URL to the favicon.
         */
        @NonNull private final String url;

        /**
         * Create a new favicon for a server.
         *
         * @param base64 the Base64 encoded favicon
         * @param hostname the server hostname
         * @return the favicon, null if none
         */
        public static Favicon create(String base64, @NonNull String hostname) {
            if (base64 == null) { // No favicon to create
                return null;
            }
            return new Favicon(
                    base64,
                    AppConfig.INSTANCE.getServerPublicUrl() + "/server/icon/" + hostname
            );
        }
    }

    /**
     * A plugin for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Plugin {
        /**
         * The name of this plugin.
         */
        @NonNull private final String name;

        /**
         * The version of this plugin.
         */
        @NonNull private final String version;
    }

    /**
     * Forge mod information for a server.
     * <p>
     * This is for servers on 1.12 or below.
     * </p>
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

    /**
     * Forge information for a server.
     * <p>
     * This is for servers on 1.13 and above.
     * </p>
     */
    @AllArgsConstructor @Getter @ToString
    public static class ForgeData {
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
}