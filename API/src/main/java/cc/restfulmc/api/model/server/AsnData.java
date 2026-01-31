package cc.restfulmc.api.model.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The ASN data of a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class AsnData {
    /**
     * The ASN number.
     */
    private final long number;

    /**
     * The ASN organization.
     */
    @NonNull private final String organization;
}