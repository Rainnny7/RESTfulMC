package cc.restfulmc.api.model.dns;

import lombok.*;

/**
 * A representation of a DNS record.
 *
 * @author Braydon
 */
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @ToString
public abstract class DNSRecord {
    /**
     * The type of this record.
     */
    @NonNull private Type type;

    /**
     * The name of this record.
     */
    @NonNull private String name;

    /**
     * The TTL (Time To Live) of this record.
     */
    private long ttl;

    /**
     * Types of a record.
     */
    public enum Type {
        A, SRV
    }
}