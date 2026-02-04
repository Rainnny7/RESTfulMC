package cc.restfulmc.api.model.server;

import cc.restfulmc.api.model.dns.DNSRecord;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * A model representing a Minecraft server.
 *
 * @author Braydon
 */
@SuperBuilder @Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
@NoArgsConstructor(force = true)
public class MinecraftServer {
    /**
     * The hostname of this server.
     */
    @EqualsAndHashCode.Include @NonNull private String hostname;

    /**
     * The IP address of this server, if resolved.
     */
    private String ip;

    /**
     * The port of this server.
     */
    @EqualsAndHashCode.Include private int port;

    /**
     * The DNS records resolved for this server, null if none.
     */
    private DNSRecord[] records;

    /**
     * The ASN data of this server, null if unknown.
     */
    private AsnData asn;

    /**
     * The Geo location of this server, null if unknown.
     */
    private GeoLocation geo;

    /**
     * The player counts of this server.
     */
    @NonNull private Players players;

    /**
     * The MOTD of this server.
     */
    @NonNull private MOTD motd;
}