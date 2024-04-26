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