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
package me.braydon.mc.common.packet.impl.bedrock;

import lombok.NonNull;
import me.braydon.mc.common.packet.MinecraftBedrockPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This packet is sent by the client to the server to
 * request a pong response from the server. The server
 * will respond with a string containing the server's status.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Raknet_Protocol#Unconnected_Ping">Protocol Docs</a>
 */
public final class BedrockPacketUnconnectedPing implements MinecraftBedrockPacket {
    private static final byte ID = 0x01; // The ID of the packet
    private static final byte[] MAGIC = { 0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120 };

    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DatagramSocket socket) throws IOException {
        // Construct the packet buffer
        ByteBuffer buffer = ByteBuffer.allocate(33).order(ByteOrder.LITTLE_ENDIAN);;
        buffer.put(ID); // Packet ID
        buffer.putLong(System.currentTimeMillis()); // Timestamp
        buffer.put(MAGIC); // Magic
        buffer.putLong(0L); // Client GUID

        // Send the packet
        socket.send(new DatagramPacket(buffer.array(), 0, buffer.limit()));
    }
}