package cc.restfulmc.api.common.packet.impl.bedrock;

import cc.restfulmc.api.common.packet.UDPPacket;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This packet is sent by the client to the server to
 * request a pong response from the server. The server
 * will respond with a string containing the server's status.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Raknet_Protocol#Unconnected_Ping">Protocol Docs</a>
 */
public final class BedrockUnconnectedPingPacket extends UDPPacket {
    private static final byte ID = 0x01; // The ID of the packet
    private static final byte[] MAGIC = { 0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120 };

    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DatagramSocket socket) throws IOException {
        // Construct the packet buffer
        ByteBuffer buffer = ByteBuffer.allocate(33).order(ByteOrder.LITTLE_ENDIAN);;
        buffer.put(ID); // Packet ID
        buffer.putLong(System.currentTimeMillis()); // Timestamp
        buffer.put(MAGIC); // Magic
        buffer.putLong(0L); // Client GUID

        // Send the packet
        socket.send(new DatagramPacket(buffer.array(), 0, buffer.limit()));
    }
}