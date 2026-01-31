package cc.restfulmc.api.model.token.server;

import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import cc.restfulmc.api.model.server.java.Version;
import lombok.NonNull;
import lombok.ToString;

/**
 * A token representing the response from
 * pinging a legacy {@link JavaMinecraftServer}.
 *
 * @author Braydon
 */
@ToString(callSuper = true)
public final class LegacyJavaServerStatusToken extends GenericJavaServerStatusToken {
    public LegacyJavaServerStatusToken(@NonNull Object description, @NonNull Players players, @NonNull Version version) {
        super(description, players, version);
    }
}