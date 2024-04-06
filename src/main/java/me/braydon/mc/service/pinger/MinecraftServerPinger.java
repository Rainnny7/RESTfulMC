package me.braydon.mc.service.pinger;

import lombok.NonNull;
import me.braydon.mc.model.MinecraftServer;

/**
 * A {@link MinecraftServerPinger} is
 * used to ping a {@link MinecraftServer}.
 *
 * @author Braydon
 * @param <T> the type of server to ping
 */
public interface MinecraftServerPinger<T extends MinecraftServer> {
    /**
     * Ping the server with the given hostname and port.
     *
     * @param hostname the hostname of the server
     * @param port the port of the server
     * @return the server that was pinged
     */
    T ping(@NonNull String hostname, int port);
}