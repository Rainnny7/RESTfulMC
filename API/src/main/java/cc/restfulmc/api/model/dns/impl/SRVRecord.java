package cc.restfulmc.api.model.dns.impl;

import cc.restfulmc.api.model.dns.DNSRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.net.InetSocketAddress;

/**
 * An SRV record implementation.
 *
 * @author Braydon
 */
@NoArgsConstructor @Setter @Getter @ToString(callSuper = true)
public final class SRVRecord extends DNSRecord {
    /**
     * The priority of this record.
     */
    private int priority;

    /**
     * The weight of this record.
     */
    private int weight;

    /**
     * The port of this record.
     */
    private int port;

    /**
     * The target of this record.
     */
    @NonNull private String target;

    public SRVRecord(@NonNull org.xbill.DNS.SRVRecord bootstrap) {
        super(Type.SRV, bootstrap.getName().toString(), bootstrap.getTTL());
        priority = bootstrap.getPriority();
        weight = bootstrap.getWeight();
        port = bootstrap.getPort();
        target = bootstrap.getTarget().toString().replaceFirst("\\.$", "");
    }

    /**
     * Get a socket address from
     * the target and port.
     *
     * @return the socket address
     */
    @NonNull @JsonIgnore
    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(target, port);
    }
}