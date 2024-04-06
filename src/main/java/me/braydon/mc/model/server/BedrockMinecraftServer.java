package me.braydon.mc.model.server;

import lombok.NonNull;
import me.braydon.mc.model.MinecraftServer;

/**
 * A Bedrock edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
public final class BedrockMinecraftServer extends MinecraftServer {
    private BedrockMinecraftServer(@NonNull String ip, int port, @NonNull Version version, @NonNull Players players) {
        super(ip, port, version, players);
    }
}