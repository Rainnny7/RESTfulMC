package cc.restfulmc.api.model.server.bedrock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Version information for a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class Version {
    /**
     * The protocol version of the server.
     */
    private final int protocol;

    /**
     * The version name of the server.
     */
    @NonNull private final String name;
}