package cc.restfulmc.api.model.server.bedrock;

import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.server.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A Bedrock edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@SuperBuilder @Getter @ToString(callSuper = true) @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(force = true)
public final class BedrockMinecraftServer extends MinecraftServer {
    /**
     * The ID of this server.
     */
    @EqualsAndHashCode.Include @NonNull private final String id;

    /**
     * The edition of this server.
     */
    @NonNull private final Edition edition;

    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The gamemode of this server.
     */
    @NonNull private final GameMode gamemode;

    /**
     * Create a new Bedrock Minecraft server.
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param records the DNS records of the server, if any
     * @param token the status token
     * @return the Bedrock Minecraft server
     */
    @NonNull
    public static BedrockMinecraftServer create(@NonNull String hostname, String ip, int port, DNSRecord[] records, @NonNull String token) {
        String[] split = token.split(";"); // Split the token
        Edition edition = Edition.valueOf(split[0]);
        Version version = new Version(Integer.parseInt(split[2]), split[3]);
        Players players = new Players(Integer.parseInt(split[4]), Integer.parseInt(split[5]), null);
        MOTD motd = MOTD.create(split[1] + "\n" + split[7], ServerPlatform.BEDROCK, hostname);
        GameMode gameMode = new GameMode(split[8], split.length > 9 ? Integer.parseInt(split[9]) : -1);
        return BedrockMinecraftServer.builder()
                .id(split[6])
                .hostname(hostname)
                .ip(ip)
                .port(port)
                .records(records)
                .edition(edition)
                .version(version)
                .players(players)
                .motd(motd)
                .gamemode(gameMode)
                .build();
    }
}