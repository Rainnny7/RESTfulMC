package cc.restfulmc.api.common.packet.impl.java.udp;

import cc.restfulmc.api.common.packet.JavaQueryPacket;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

/**
 * This packet is sent by the server to the client in
 * response to the {@link JavaQueryFullStatRequestPacket}.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Query#Response_3">Query Protocol Docs</a>
 */
@Getter
public final class JavaQueryFullStatResponsePacket extends JavaQueryPacket {
    /**
     * The response from the server, null if none.
     */
    private Map<String, String> response;

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

        // Construct a response from the received packet.
        Map<String, String> response = new HashMap<>();

        String previousEntry = null;
        for (byte[] bytes : split(trim(receivePacket.getData()))) {
            String entry = new String(bytes); // The entry

            if (previousEntry != null) {
                response.put(previousEntry, entry);
                previousEntry = null;
                continue;
            }
            previousEntry = entry;
        }
        this.response = response;
    }
}