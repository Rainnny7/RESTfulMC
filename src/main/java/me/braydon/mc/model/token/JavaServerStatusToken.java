package me.braydon.mc.model.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.braydon.mc.model.MinecraftServer;
import me.braydon.mc.model.server.JavaMinecraftServer;

/**
 * A token representing the response from
 * pinging a {@link JavaMinecraftServer}.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class JavaServerStatusToken {
    /**
     * The description (MOTD) of this server.
     * <p>
     * Legacy: String, New: JSON Object
     * </p>
     */
    @NonNull private final Object description;

    /**
     * The base64 encoded favicon of this server, null if no favicon.
     */
    private final String favicon;

    /**
     * The version information of this server.
     */
    @NonNull private final MinecraftServer.Version version;

    /**
     * The player counts of this server.
     */
    @NonNull private final MinecraftServer.Players players;

    /**
     * Does this server enforce secure chat?
     */
    private final boolean enforcesSecureChat;

    /**
     * Is this server preventing chat reports?
     */
    private final boolean preventsChatReports;
}