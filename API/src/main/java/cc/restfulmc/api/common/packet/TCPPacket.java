package cc.restfulmc.api.common.packet;

import lombok.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a packet in the
 * Minecraft Java protocol.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Protocol">Protocol Docs</a>
 */
public abstract class TCPPacket {
    /**
     * Process this packet.
     *
     * @param inputStream the input stream to read from
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    public abstract void process(@NonNull DataInputStream inputStream, @NonNull DataOutputStream outputStream) throws IOException;

    /**
     * Write a variable integer to the output stream.
     *
     * @param outputStream the output stream to write to
     * @param paramInt the integer to write
     * @throws IOException if an I/O error occurs
     */
    protected final void writeVarInt(DataOutputStream outputStream, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                outputStream.writeByte(paramInt);
                return;
            }
            outputStream.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    /**
     * Read a variable integer from the input stream.
     *
     * @param inputStream the input stream to read from
     * @return the integer that was read
     * @throws IOException if an I/O error occurs
     */
    protected final int readVarInt(@NonNull DataInputStream inputStream) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = inputStream.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
            if ((k & 0x80) != 128) {
                break;
            }
        }
        return i;
    }
}