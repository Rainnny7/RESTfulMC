package me.braydon.mc.model.server;

import lombok.NonNull;
import me.braydon.mc.model.MinecraftServer;
import me.braydon.mc.model.token.JavaServerStatusToken;

/**
 * A Java edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
public final class JavaMinecraftServer extends MinecraftServer {
    private JavaMinecraftServer(@NonNull String ip, int port, @NonNull Version version, @NonNull Players players) {
        super(ip, port, version, players);
    }

    public static JavaMinecraftServer create(@NonNull String ip, int port, @NonNull JavaServerStatusToken token) {
        return new JavaMinecraftServer(ip, port, token.getVersion(), token.getPlayers());
    }
}