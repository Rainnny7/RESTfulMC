/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.service.pinger.impl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.RESTfulMC;
import me.braydon.mc.common.DNSUtils;
import me.braydon.mc.common.packet.impl.java.JavaPacketHandshakingInSetProtocol;
import me.braydon.mc.common.packet.impl.java.JavaPacketStatusInStart;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.server.JavaMinecraftServer;
import me.braydon.mc.model.token.JavaServerStatusToken;
import me.braydon.mc.service.pinger.MinecraftServerPinger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * The {@link MinecraftServerPinger} for pinging
 * {@link JavaMinecraftServer}'s over TCP.
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
     * @throws BadRequestException if the hostname is unknown
     * @throws ResourceNotFoundException if the server could not be found
     */
    @Override
    public JavaMinecraftServer ping(@NonNull String hostname, int port) throws BadRequestException, ResourceNotFoundException {
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
                new JavaPacketHandshakingInSetProtocol(hostname, port, 47).process(inputStream, outputStream);

                // Send the status request to the server, and await back the response
                JavaPacketStatusInStart packetStatusInStart = new JavaPacketStatusInStart();
                packetStatusInStart.process(inputStream, outputStream);
                JavaServerStatusToken token = RESTfulMC.GSON.fromJson(packetStatusInStart.getResponse(), JavaServerStatusToken.class);
                return JavaMinecraftServer.create(hostname, ip, port, token); // Return the server
            }
        } catch (IOException ex) {
            if (ex instanceof UnknownHostException) {
                throw new BadRequestException("Unknown hostname: %s".formatted(hostname));
            } else if (ex instanceof ConnectException || ex instanceof SocketTimeoutException) {
                throw new ResourceNotFoundException(ex);
            }
            log.error("An error occurred pinging %s:%s:".formatted(hostname, port), ex);
        }
        return null;
    }
}