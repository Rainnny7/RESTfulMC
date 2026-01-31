package cc.restfulmc.api.model.server.bedrock;

import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.server.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A Bedrock edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@Getter @ToString(callSuper = true) @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
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

    private BedrockMinecraftServer(@NonNull String id, @NonNull String hostname, String ip, int port, AsnData asn,
                                   GeoLocation geo, DNSRecord[] records, @NonNull Edition edition, @NonNull Version version,
                                   @NonNull Players players, @NonNull MOTD motd, @NonNull GameMode gamemode) {
        super(hostname, ip, port, records, asn, geo, players, motd);
        this.id = id;
        this.edition = edition;
        this.version = version;
        this.gamemode = gamemode;
    }

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
        MOTD motd = MOTD.create(split[1] + "\n" + split[7]);
        GameMode gameMode = new GameMode(split[8], split.length > 9 ? Integer.parseInt(split[9]) : -1);
        return new BedrockMinecraftServer(split[6], hostname, ip, port, null, null, records, edition, version, players, motd, gameMode);
    }
}