package cc.restfulmc.api.service.pinger.impl;

import cc.restfulmc.api.common.packet.impl.bedrock.BedrockUnconnectedPingPacket;
import cc.restfulmc.api.common.packet.impl.bedrock.BedrockUnconnectedPongPacket;
import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.server.bedrock.BedrockMinecraftServer;
import cc.restfulmc.api.service.pinger.MinecraftServerPinger;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * The {@link MinecraftServerPinger} for pinging
 * {@link BedrockMinecraftServer} over UDP.
 *
 * @author Braydon
 */
@Log4j2(topic = "Bedrock MC Server Pinger")
public final class BedrockMinecraftServerPinger implements MinecraftServerPinger<BedrockMinecraftServer> {
    private static final int TIMEOUT = 3000; // The timeout for the socket

    /**
     * Ping the server with the given hostname and port.
     *
     * @param hostname the hostname of the server
     * @param ip       the ip of the server, null if unresolved
     * @param port     the port of the server
     * @param records  the DNS records of the server
     * @return the server that was pinged
     */
    @Override
    public BedrockMinecraftServer ping(@NonNull String hostname, String ip, int port, @NonNull DNSRecord[] records) {
        log.info("Opening UDP connection to {}:{}...", hostname, port);
        long before = System.currentTimeMillis(); // Timestamp before pinging

        // Open a socket connection to the server
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);
            socket.connect(new InetSocketAddress(hostname, port));

            long ping = System.currentTimeMillis() - before; // Calculate the ping
            log.info("UDP Connection to {}:{} opened. Ping: {}ms", hostname, port, ping);

            // Send the unconnected ping packet
            new BedrockUnconnectedPingPacket().process(socket);

            // Handle the received unconnected pong packet
            BedrockUnconnectedPongPacket unconnectedPong = new BedrockUnconnectedPongPacket();
            unconnectedPong.process(socket);
            String response = unconnectedPong.getResponse();
            if (response == null) { // No pong response
                throw new ResourceNotFoundException("Server didn't respond to ping");
            }
            return BedrockMinecraftServer.create(hostname, ip, port, records, response); // Return the server
        } catch (IOException ex) {
            if (ex instanceof UnknownHostException) {
                throw new BadRequestException("Unknown hostname: %s".formatted(hostname));
            } else if (ex instanceof SocketTimeoutException) {
                throw new ResourceNotFoundException(ex);
            }
            log.error("An error occurred pinging {}:{}:", hostname, port, ex);
        }
        return null;
    }
}