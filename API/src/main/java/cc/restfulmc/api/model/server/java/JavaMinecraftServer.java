package cc.restfulmc.api.model.server.java;

import cc.restfulmc.api.common.Constants;
import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.server.*;
import cc.restfulmc.api.model.token.server.GenericJavaServerStatusToken;
import cc.restfulmc.api.model.token.server.JavaServerChallengeStatusToken;
import cc.restfulmc.api.model.token.server.JavaServerStatusToken;
import cc.restfulmc.api.model.token.server.LegacyJavaServerStatusToken;
import cc.restfulmc.api.service.MojangService;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Java edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@SuperBuilder @Setter @Getter @ToString(callSuper = true)
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
     * Is this a legacy Beta 1.8 to 1.6 server?
     */
    private final boolean legacyServer;

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
     * Whether this is a modded server.
     */
    private final boolean modded;

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
                                             @NonNull GenericJavaServerStatusToken statusToken, JavaServerChallengeStatusToken challengeStatusToken) {
        String motdString = statusToken.getDescription() instanceof String ? (String) statusToken.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = LegacyComponentSerializer.builder()
                    .character(LegacyComponentSerializer.SECTION_CHAR)
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build()
                    .serialize(GsonComponentSerializer.gson().deserialize(Constants.GSON.toJson(statusToken.getDescription())));
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

        Favicon favicon = null;
        ModInfo modInfo = null;
        ForgeData forgeData = null;
        boolean previewsChat = false;
        boolean enforcesSecureChat = false;
        boolean preventsChatReports = false;
        boolean isModded = false;
        if (statusToken instanceof JavaServerStatusToken nativeStatusToken) {
            favicon = Favicon.create(nativeStatusToken.getFavicon(), hostname);
            modInfo = nativeStatusToken.getModInfo();
            forgeData = nativeStatusToken.getForgeData();
            previewsChat = nativeStatusToken.isPreviewsChat();
            enforcesSecureChat = nativeStatusToken.isEnforcesSecureChat();
            preventsChatReports = nativeStatusToken.isPreventsChatReports();
            isModded = nativeStatusToken.isModded();
        }
        return JavaMinecraftServer.builder()
                .hostname(hostname)
                .ip(ip)
                .port(port)
                .records(records)
                .players(Players.create(statusToken.getPlayers()))
                .motd(MOTD.create(motdString, ServerPlatform.JAVA, hostname))
                .version(statusToken.getVersion().detailedCopy())
                .favicon(favicon)
                .software(software)
                .plugins(plugins)
                .modInfo(modInfo)
                .forgeData(forgeData)
                .world(world)
                .legacyServer(statusToken instanceof LegacyJavaServerStatusToken)
                .queryEnabled(challengeStatusToken != null)
                .previewsChat(previewsChat)
                .enforcesSecureChat(enforcesSecureChat)
                .preventsChatReports(preventsChatReports)
                .modded(isModded)
                .mojangBanned(false)
                .build();
    }
}