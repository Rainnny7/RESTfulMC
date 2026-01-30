package cc.restfulmc.api.common.packet;

import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * Represents a packet in the
 * Minecraft Bedrock protocol.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Raknet_Protocol">Protocol Docs</a>
 */
public abstract class UDPPacket {
    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    public abstract void process(@NonNull DatagramSocket socket) throws IOException;
}