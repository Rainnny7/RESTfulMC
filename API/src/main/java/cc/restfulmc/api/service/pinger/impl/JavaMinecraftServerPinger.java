package cc.restfulmc.api.service.pinger.impl;

import cc.restfulmc.api.common.JavaMinecraftVersion;
import cc.restfulmc.api.common.packet.impl.java.tcp.JavaHandshakingInSetProtocolPacket;
import cc.restfulmc.api.common.packet.impl.java.tcp.JavaStatusInStartPacket;
import cc.restfulmc.api.common.packet.impl.java.udp.JavaQueryFullStatRequestPacket;
import cc.restfulmc.api.common.packet.impl.java.udp.JavaQueryFullStatResponsePacket;
import cc.restfulmc.api.common.packet.impl.java.udp.JavaQueryHandshakeRequestPacket;
import cc.restfulmc.api.common.packet.impl.java.udp.JavaQueryHandshakeResponsePacket;
import cc.restfulmc.api.config.AppConfig;
import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.server.JavaMinecraftServer;
import cc.restfulmc.api.model.token.JavaServerChallengeStatusToken;
import cc.restfulmc.api.model.token.JavaServerStatusToken;
import cc.restfulmc.api.service.pinger.MinecraftServerPinger;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * The {@link MinecraftServerPinger} for pinging
 * {@link JavaMinecraftServer}'s over TCP/UDP.
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
     * @param ip       the ip of the server, null if unresolved
     * @param port     the port of the server
     * @param records  the DNS records of the server
     * @return the server that was pinged
     */
    @Override
    public JavaMinecraftServer ping(@NonNull String hostname, String ip, int port, @NonNull DNSRecord[] records) {
        log.info("Pinging {}:{}...", hostname, port);

        try {
            // Ping the server and retrieve both the status token, and the challenge status token
            JavaServerStatusToken statusToken = retrieveStatusToken(hostname, port);
            JavaServerChallengeStatusToken challengeStatusToken = null;
            try {
                 challengeStatusToken = retrieveChallengeStatusToken(hostname, port);
            } catch (Exception ex) {
                // An exception will be raised if querying
                // is disabled on the server. If the exception
                // is not caused by querying being disabled, we
                // want to log the error.
                if (!(ex instanceof IOException)) {
                    log.error("Failed retrieving challenge status token for %s:%s:".formatted(hostname, port), ex);
                }
            }

            // Return the server
            return JavaMinecraftServer.create(hostname, ip, port, records, statusToken, challengeStatusToken);
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

    /**
     * Ping a server and retrieve its response.
     *
     * @param hostname the hostname to ping
     * @param port the port to ping
     * @return the status token
     * @throws IOException if an I/O error occurs
     * @throws ResourceNotFoundException if the server didn't respond
     */
    @NonNull
    private JavaServerStatusToken retrieveStatusToken(@NonNull String hostname, int port) throws IOException, ResourceNotFoundException {
        log.info("Opening TCP connection to {}:{}...", hostname, port);
        long before = System.currentTimeMillis(); // Timestamp before pinging

        // Open a socket connection to the server
        try (Socket socket = new Socket()) {
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(hostname, port), TIMEOUT);

            long ping = System.currentTimeMillis() - before; // Calculate the ping
            log.info("TCP Connection to {}:{} opened. Ping: {}ms", hostname, port, ping);

            // Begin packet transaction
            try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                // Begin handshaking with the server
                new JavaHandshakingInSetProtocolPacket(hostname, port, JavaMinecraftVersion.getMinimumVersion().getProtocol())
                        .process(inputStream, outputStream);

                // Send the status request to the server, and await back the response
                JavaStatusInStartPacket packetStatusInStart = new JavaStatusInStartPacket();
                packetStatusInStart.process(inputStream, outputStream);
                String response = packetStatusInStart.getResponse();
                if (response == null) { // No response
                    throw new ResourceNotFoundException("Server didn't respond to status request");
                }
                return AppConfig.GSON.fromJson(response, JavaServerStatusToken.class);
            }
        }
    }

    /**
     * Ping a server and retrieve its challenge status token.
     *
     * @param hostname the hostname to ping
     * @param port the port to ping
     * @return the challenge token
     * @throws IOException if an I/O error occurs
     */
    @NonNull
    private JavaServerChallengeStatusToken retrieveChallengeStatusToken(@NonNull String hostname, int port) throws IOException {
        log.info("Opening UDP connection to {}:{}...", hostname, port);
        long before = System.currentTimeMillis(); // Timestamp before pinging

        // Open a socket connection to the server
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(500);
            socket.connect(new InetSocketAddress(hostname, port));

            long ping = System.currentTimeMillis() - before; // Calculate the ping
            log.info("UDP Connection to {}:{} opened. Ping: {}ms", hostname, port, ping);

            // Begin handshaking with the server
            new JavaQueryHandshakeRequestPacket().process(socket);
            JavaQueryHandshakeResponsePacket handshakeResponse = new JavaQueryHandshakeResponsePacket();
            handshakeResponse.process(socket);

            // Send the full stats request to the server, and await back the response
            new JavaQueryFullStatRequestPacket(handshakeResponse.getResponse()).process(socket);
            JavaQueryFullStatResponsePacket fullStatResponse = new JavaQueryFullStatResponsePacket();
            fullStatResponse.process(socket);

            // Return the challenge token
            return JavaServerChallengeStatusToken.create(fullStatResponse.getResponse());
        }
    }
}