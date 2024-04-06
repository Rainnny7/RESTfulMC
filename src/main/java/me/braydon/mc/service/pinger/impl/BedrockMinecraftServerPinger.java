package me.braydon.mc.service.pinger.impl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.model.server.BedrockMinecraftServer;
import me.braydon.mc.service.pinger.MinecraftServerPinger;

/**
 * The {@link MinecraftServerPinger} for
 * pinging {@link BedrockMinecraftServer}'s.
 *
 * @author Braydon
 */
@Log4j2(topic = "Bedrock MC Server Pinger")
public final class BedrockMinecraftServerPinger implements MinecraftServerPinger<BedrockMinecraftServer> {
    /**
     * Ping the server with the given hostname and port.
     *
     * @param hostname the hostname of the server
     * @param port     the port of the server
     * @return the server that was pinged
     */
    @Override
    public BedrockMinecraftServer ping(@NonNull String hostname, int port) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}