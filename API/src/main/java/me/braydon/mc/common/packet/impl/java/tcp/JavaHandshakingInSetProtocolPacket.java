/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.common.packet.impl.java.tcp;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import me.braydon.mc.common.packet.TCPPacket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This packet is sent by the client to the server to set
 * the hostname, port, and protocol version of the client.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Protocol#Handshake">Protocol Docs</a>
 */
@AllArgsConstructor @ToString
public final class JavaHandshakingInSetProtocolPacket extends TCPPacket {
    private static final byte ID = 0x00; // The ID of the packet
    private static final int STATUS_HANDSHAKE = 1; // The status handshake ID

    /**
     * The hostname of the server.
     */
    @NonNull private final String hostname;

    /**
     * The port of the server.
     */
    private final int port;

    /**
     * The protocol version of the server.
     */
    private final int protocolVersion;

    /**
     * Process this packet.
     *
     * @param inputStream  the input stream to read from
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DataInputStream inputStream, @NonNull DataOutputStream outputStream) throws IOException {
        try (ByteArrayOutputStream handshakeBytes = new ByteArrayOutputStream();
             DataOutputStream handshake = new DataOutputStream(handshakeBytes)
        ) {
            handshake.writeByte(ID); // Write the ID of the packet
            writeVarInt(handshake, protocolVersion); // Write the protocol version
            writeVarInt(handshake, hostname.length()); // Write the length of the hostname
            handshake.writeBytes(hostname); // Write the hostname
            handshake.writeShort(port); // Write the port
            writeVarInt(handshake, STATUS_HANDSHAKE); // Write the status handshake ID

            // Write the handshake bytes to the output stream
            writeVarInt(outputStream, handshakeBytes.size());
            outputStream.write(handshakeBytes.toByteArray());
        }
    }
}