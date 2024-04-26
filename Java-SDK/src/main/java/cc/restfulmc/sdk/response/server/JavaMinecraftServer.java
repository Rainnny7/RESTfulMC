/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cc.restfulmc.sdk.response.server;

import cc.restfulmc.sdk.response.server.dns.DNSRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A representation of a Java Edition Minecraft server.
 *
 * @author Braydon
 */
@Getter @ToString(callSuper = true)
public class JavaMinecraftServer extends MinecraftServer {
    /**
     * The version of this server.
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
     * The legacy Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.12 or below.
     * </p>
     */
    private final ModInfo modInfo;

    /**
     * The modern Forge mod information for this server, null if none.
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
     *
     * @see <a href="https://wiki.vg/Mojang_API#Blocked_Servers">Mojang API</a>
     */
    private final boolean mojangBanned;

    public JavaMinecraftServer(@NonNull String hostname, String ip, int port, DNSRecord[] records, GeoLocation geo,
                               @NonNull Players players, @NonNull MOTD motd, @NonNull Version version, Favicon favicon,
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
     * Version information for a Java server.
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
     * Legacy Forge mod information for a server.
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
        private final Mod[] mods;

        /**
         * A Forge mod for a server.
         */
        @AllArgsConstructor @Getter @ToString
        private static class Mod {
            /**
             * The name of this mod.
             */
            @NonNull private final String name;

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
            @NonNull private final String name;

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
            @NonNull private final String name;

            /**
             * The marker for this mod.
             */
            @NonNull private final String marker;
        }
    }
}