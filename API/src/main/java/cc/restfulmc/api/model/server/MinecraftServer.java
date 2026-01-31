package cc.restfulmc.api.model.server;

import cc.restfulmc.api.model.dns.DNSRecord;
import lombok.*;

/**
 * A model representing a Minecraft server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class MinecraftServer {
    /**
     * The hostname of this server.
     */
    @EqualsAndHashCode.Include @NonNull private final String hostname;

    /**
     * The IP address of this server, if resolved.
     */
    private final String ip;

    /**
     * The port of this server.
     */
    @EqualsAndHashCode.Include private final int port;

    /**
     * The DNS records resolved for this server, null if none.
     */
    private final DNSRecord[] records;

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
    @NonNull private final Players players;

    /**
     * The MOTD of this server.
     */
    @NonNull private final MOTD motd;
}