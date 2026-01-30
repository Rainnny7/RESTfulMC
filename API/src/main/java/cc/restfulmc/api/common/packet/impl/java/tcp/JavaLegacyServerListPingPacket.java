package cc.restfulmc.api.common.packet.impl.java.tcp;

import cc.restfulmc.api.common.packet.TCPPacket;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This packet implements the legacy server list ping protocol
 * used from Beta 1.8 to 1.6. Sends 0xFE 0x01 and handles both
 * the 1.4+ response format (null-separated) and the older
 * Beta 1.8-1.3 format (§ separated).
 *
 * @author Braydon
 * @see <a href="https://minecraft.wiki/w/Java_Edition_protocol/Server_List_Ping#Beta_1.8_to_1.3">Protocol Docs</a>
 */
@Getter @ToString
public final class JavaLegacyServerListPingPacket extends TCPPacket {
    private static final int KICK_PACKET_ID = 0xFF;

    /**
     * The ping version from the server (-1 if using old format).
     */
    private int pingVersion = -1;

    /**
     * The protocol version from the server (-1 if using old format).
     */
    private int protocolVersion = -1;

    /**
     * The game version from the server (null if using old format).
     */
    private String gameVersion;

    /**
     * The MOTD from the server, null if none.
     */
    private String motd;

    /**
     * The number of online players.
     */
    private int onlinePlayers = -1;

    /**
     * The maximum number of players.
     */
    private int maxPlayers = -1;

    /**
     * Process this packet.
     *
     * @param inputStream  the input stream to read from
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DataInputStream inputStream, @NonNull DataOutputStream outputStream) throws IOException {
        // Send the legacy ping request (0xFE 0x01 for 1.4-1.5 compatibility)
        outputStream.write(new byte[] { (byte) 0xFE, (byte) 0x01 });
        outputStream.flush();

        // Read the kick packet ID
        int packetId = inputStream.readUnsignedByte();
        if (packetId != KICK_PACKET_ID) {
            throw new IOException("Invalid packet ID (0x" + Integer.toHexString(packetId) + "), expected 0xFF.");
        }

        // Read the string length (in UTF-16 code units) as a short
        int length = inputStream.readUnsignedShort();
        if (length == 0) {
            throw new IOException("Invalid string length.");
        }

        // Read the response bytes (2 bytes per UTF-16 code unit)
        byte[] data = new byte[length * 2];
        inputStream.readFully(data);
        String response = new String(data, StandardCharsets.UTF_16BE);

        // Check which format the response is in
        if (response.startsWith("§")) {
            // 1.4+ format: §1\0protocolVersion\0gameVersion\0motd\0playersOnline\0maxPlayers
            String[] parts = response.split("\0");
            pingVersion = Integer.parseInt(parts[0].substring(1));
            protocolVersion = Integer.parseInt(parts[1]);
            gameVersion = parts[2];
            motd = parts[3];
            onlinePlayers = Integer.parseInt(parts[4]);
            maxPlayers = Integer.parseInt(parts[5]);
        } else {
            // Beta 1.8-1.3 format: motd§playersOnline§maxPlayers
            String[] parts = response.split("§");
            if (parts.length < 3) {
                throw new IOException("Invalid legacy ping response format: " + response);
            }
            motd = parts[0];
            onlinePlayers = Integer.parseInt(parts[1]);
            maxPlayers = Integer.parseInt(parts[2]);
        }
    }
}
