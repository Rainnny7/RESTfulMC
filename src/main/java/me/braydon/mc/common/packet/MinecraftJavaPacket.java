/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny), and the RESTfulMC contributors
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
package me.braydon.mc.common.packet;

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
public abstract class MinecraftJavaPacket {
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