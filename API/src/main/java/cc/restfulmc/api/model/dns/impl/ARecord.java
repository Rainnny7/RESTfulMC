package cc.restfulmc.api.model.dns.impl;

import cc.restfulmc.api.model.dns.DNSRecord;
import lombok.*;

import java.net.InetAddress;

/**
 * An A record implementation.
 *
 * @author Braydon
 */
@NoArgsConstructor @Setter @Getter @ToString(callSuper = true)
public final class ARecord extends DNSRecord {
    /**
     * The address of this record, null if unresolved.
     */
    private String address;

    public ARecord(@NonNull org.xbill.DNS.ARecord bootstrap) {
        super(Type.A, bootstrap.getName().toString(), bootstrap.getTTL());
        InetAddress address = bootstrap.getAddress();
        this.address = address == null ? null : address.getHostAddress();
    }
}