package cc.restfulmc.api.common.packet.impl.bedrock;

import cc.restfulmc.api.common.packet.UDPPacket;
import cc.restfulmc.api.model.server.bedrock.Edition;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * This packet is sent by the server to the client in
 * response to the {@link BedrockUnconnectedPingPacket}.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Raknet_Protocol#Unconnected_Pong">Protocol Docs</a>
 */
@Getter
public final class BedrockUnconnectedPongPacket extends UDPPacket {
    private static final byte ID = 0x1C; // The ID of the packet

    /**
     * The response from the server, null if none.
     */
    private String response;

    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DatagramSocket socket) throws IOException {
        // Handle receiving of the packet
        byte[] receiveData = new byte[2048];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        // Construct a buffer from the received packet
        ByteBuffer buffer = ByteBuffer.wrap(receivePacket.getData()).order(ByteOrder.LITTLE_ENDIAN);
        byte id = buffer.get(); // The received packet id
        if (id == ID) {
            String response = new String(buffer.array(), StandardCharsets.UTF_8).trim(); // Extract the response

            // Trim the length of the response (short) from the
            // start of the string, which begins with the edition name
            for (Edition edition : Edition.values()) {
                int startIndex = response.indexOf(edition.name());
                if (startIndex != -1) {
                    response = response.substring(startIndex);
                    break;
                }
            }
            this.response = response;
        }
    }
}