package cc.restfulmc.api.common.packet.impl.java.udp;

import cc.restfulmc.api.common.packet.JavaQueryPacket;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This packet is sent by the client to the
 * server to request a handshake. This will
 * then allow us to send further packets such
 * as {@link JavaQueryFullStatRequestPacket}.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Query#Request">Query Protocol Docs</a>
 */
public final class JavaQueryHandshakeRequestPacket extends JavaQueryPacket {
    private static final int ID = 9; // The ID of the packet

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
            dataOutputStream.write(new byte[] {}); // No payload data

            // Send the packet
            byte[] bytes = arrayOutputStream.toByteArray();
            bytes = padArrayEnd(bytes, 11 - bytes.length);
            socket.send(new DatagramPacket(bytes, bytes.length));
        }
    }
}