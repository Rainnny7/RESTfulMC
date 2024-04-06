package me.braydon.mc.common.packet.impl;

import lombok.Getter;
import lombok.NonNull;
import me.braydon.mc.common.packet.MinecraftPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This packet is sent by the client to the server to request the
 * status of the server. The server will respond with a json object
 * containing the server's status.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Protocol#Status_Request">Protocol Docs</a>
 */
@Getter
public final class PacketStatusInStart extends MinecraftPacket {
    private static final byte ID = 0x00; // The ID of the packet

    /**
     * The response json from the server, null if none.
     */
    private String response;

    /**
     * Process this packet.
     *
     * @param inputStream  the input stream to read from
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DataInputStream inputStream, @NonNull DataOutputStream outputStream) throws IOException {
        // Send the status request
        outputStream.writeByte(0x01); // Size of packet
        outputStream.writeByte(ID);

        // Read the status response
        readVarInt(inputStream); // Size of the response
        int id = readVarInt(inputStream);
        if (id == -1) { // The stream was prematurely ended
            throw new IOException("Server prematurely ended stream.");
        } else if (id != ID) { // Invalid packet ID
            throw new IOException("Server returned invalid packet ID.");
        }

        int length = readVarInt(inputStream); // Length of the response
        if (length == -1) { // The stream was prematurely ended
            throw new IOException("Server prematurely ended stream.");
        } else if (length == 0) {
            throw new IOException("Server returned unexpected value.");
        }

        // Get the json response
        byte[] data = new byte[length];
        inputStream.readFully(data);
        response = new String(data);
    }
}