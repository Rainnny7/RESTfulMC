package me.braydon.mc.service.pinger.impl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.RESTfulMC;
import me.braydon.mc.common.DNSUtils;
import me.braydon.mc.common.packet.impl.PacketHandshakingInSetProtocol;
import me.braydon.mc.common.packet.impl.PacketStatusInStart;
import me.braydon.mc.model.server.JavaMinecraftServer;
import me.braydon.mc.model.token.JavaServerStatusToken;
import me.braydon.mc.service.pinger.MinecraftServerPinger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The {@link MinecraftServerPinger} for
 * pinging {@link JavaMinecraftServer}'s.
 *
 * @author Braydon
 */
@Log4j2(topic = "Java MC Server Pinger")
public final class JavaMinecraftServerPinger implements MinecraftServerPinger<JavaMinecraftServer> {
    private static final int TIMEOUT = 3000; // The timeout for the socket

    /**
     * Ping the server with the given hostname and port.
     *
     * @param hostname the hostname of the server
     * @param port     the port of the server
     * @return the server that was pinged
     */
    @Override
    public JavaMinecraftServer ping(@NonNull String hostname, int port) {
        InetAddress inetAddress = DNSUtils.resolveA(hostname); // Resolve the hostname to an IP address
        String ip = inetAddress == null ? null : inetAddress.getHostAddress(); // Get the IP address
        if (ip != null) { // Was the IP resolved?
            log.info("Resolved hostname: {} -> {}", hostname, ip);
        }
        log.info("Pinging {}:{}...", hostname, port);
        long before = System.currentTimeMillis(); // Timestamp before pinging

        // Open a socket connection to the server
        try (Socket socket = new Socket()) {
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(hostname, port), TIMEOUT);
            long ping = System.currentTimeMillis() - before; // Calculate the ping
            log.info("Pinged {}:{} in {}ms", hostname, port, ping);

            // Open data streams to begin packet transaction
            try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                // Begin handshaking with the server
                new PacketHandshakingInSetProtocol(hostname, port, 47).process(inputStream, outputStream);

                // Send the status request to the server, and await back the response
                PacketStatusInStart packetStatusInStart = new PacketStatusInStart();
                packetStatusInStart.process(inputStream, outputStream);
                JavaServerStatusToken token = RESTfulMC.GSON.fromJson(packetStatusInStart.getResponse(), JavaServerStatusToken.class);
                return JavaMinecraftServer.create(hostname, ip, port, token); // Return the server
            }
        } catch (IOException ex) {
            log.error("An error occurred pinging %s:%s:".formatted(hostname, port), ex);
        }
        return null;
    }
}