package cc.restfulmc.api.common.packet.impl.java.udp;

import cc.restfulmc.api.common.packet.JavaQueryPacket;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This packet is sent by the client to the server to request the
 * full stats of the server. The server will respond with a payload
 * containing the server's stats.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Query#Request_3">Query Protocol Docs</a>
 */
@AllArgsConstructor
public final class JavaQueryFullStatRequestPacket extends JavaQueryPacket {
    private static final int ID = 0; // The ID of the packet

    /**
     * The response from the {@link JavaQueryHandshakeRequestPacket}.
     */
    private final byte[] handshakeResponse;

    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DatagramSocket socket) throws IOException {
        try (
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(1460);
                DataOutputStream dataOutputStream = new DataOutputStream(arrayOutputStream)
        ) {
            dataOutputStream.write(MAGIC);
            dataOutputStream.write(ID); // Packet ID
            dataOutputStream.writeInt(1); // Session ID
            dataOutputStream.write(padArrayEnd(handshakeResponse, 4)); // The handshake response payload

            // Send the packet
            byte[] bytes = arrayOutputStream.toByteArray();
            socket.send(new DatagramPacket(bytes, bytes.length));
        }
    }
}