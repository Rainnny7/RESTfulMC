package cc.restfulmc.api.common.packet.impl.java.udp;

import cc.restfulmc.api.common.packet.JavaQueryPacket;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This packet is sent by the server to the client in
 * response to the {@link JavaQueryHandshakeRequestPacket}.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Query#Response">Query Protocol Docs</a>
 */
@Getter
public final class JavaQueryHandshakeResponsePacket extends JavaQueryPacket {
    /**
     * The response from the server.
     */
    private byte[] response;

    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DatagramSocket socket) throws IOException {
        // Handle receiving of the packet
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        // Set the response to the integer value of the received data
        response = intToBytes(Integer.parseInt(new String(receivePacket.getData()).trim()));
    }
}