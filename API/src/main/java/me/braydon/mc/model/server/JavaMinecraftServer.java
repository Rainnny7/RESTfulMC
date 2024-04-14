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
package me.braydon.mc.model.server;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import me.braydon.mc.common.JavaMinecraftVersion;
import me.braydon.mc.config.AppConfig;
import me.braydon.mc.model.MinecraftServer;
import me.braydon.mc.model.dns.DNSRecord;
import me.braydon.mc.model.token.JavaServerStatusToken;
import me.braydon.mc.service.MojangService;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

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

    private JavaMinecraftServer(@NonNull String hostname, String ip, int port, @NonNull DNSRecord[] records, @NonNull Version version,
                                @NonNull Players players, @NonNull MOTD motd, Favicon favicon, ModInfo modInfo, ForgeData forgeData,
                                boolean previewsChat, boolean enforcesSecureChat, boolean preventsChatReports, boolean mojangBanned) {
        super(hostname, ip, port, records, players, motd);
        this.version = version;
        this.favicon = favicon;
        this.modInfo = modInfo;
        this.forgeData = forgeData;
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
     * @param records the DNS records of the server
     * @param token the status token
     * @return the Java Minecraft server
     */
    @NonNull
    public static JavaMinecraftServer create(@NonNull String hostname, String ip, int port,
                                             @NonNull DNSRecord[] records, @NonNull JavaServerStatusToken token) {
        String motdString = token.getDescription() instanceof String ? (String) token.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = new TextComponent(ComponentSerializer.parse(AppConfig.GSON.toJson(token.getDescription()))).toLegacyText();
        }
        return new JavaMinecraftServer(hostname, ip, port, records, token.getVersion().detailedCopy(), Players.create(token.getPlayers()),
                MOTD.create(motdString), Favicon.create(token.getFavicon(), hostname), token.getModInfo(), token.getForgeData(),
                token.isPreviewsChat(), token.isEnforcesSecureChat(), token.isPreventsChatReports(), false
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