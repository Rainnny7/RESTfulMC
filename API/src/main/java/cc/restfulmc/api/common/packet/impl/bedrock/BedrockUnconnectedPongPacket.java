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
package cc.restfulmc.api.common.packet.impl.bedrock;

import lombok.Getter;
import lombok.NonNull;
import cc.restfulmc.api.common.packet.UDPPacket;
import cc.restfulmc.api.model.server.BedrockMinecraftServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * This packet is sent by the server to the client in
 * response to the {@link BedrockUnconnectedPingPacket}.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Raknet_Protocol#Unconnected_Pong">Protocol Docs</a>
 */
@Getter
public final class BedrockUnconnectedPongPacket extends UDPPacket {
    private static final byte ID = 0x1C; // The ID of the packet

    /**
     * The response from the server, null if none.
     */
    private String response;

    /**
     * Process this packet.
     *
     * @param socket the socket to process the packet for
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void process(@NonNull DatagramSocket socket) throws IOException {
        // Handle receiving of the packet
        byte[] receiveData = new byte[2048];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        // Construct a buffer from the received packet
        ByteBuffer buffer = ByteBuffer.wrap(receivePacket.getData()).order(ByteOrder.LITTLE_ENDIAN);
        byte id = buffer.get(); // The received packet id
        if (id == ID) {
            String response = new String(buffer.array(), StandardCharsets.UTF_8).trim(); // Extract the response

            // Trim the length of the response (short) from the
            // start of the string, which begins with the edition name
            for (BedrockMinecraftServer.Edition edition : BedrockMinecraftServer.Edition.values()) {
                int startIndex = response.indexOf(edition.name());
                if (startIndex != -1) {
                    response = response.substring(startIndex);
                    break;
                }
            }
            this.response = response;
        }
    }
}