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
package me.braydon.mc.common.packet;

import java.util.ArrayList;

/**
 * @author Braydon
 */
public abstract class JavaQueryPacket extends UDPPacket {
    protected static byte[] MAGIC = { (byte) 0xFE, (byte) 0xFD };

    protected final byte[] padArrayEnd(byte[] array, int amount) {
        byte[] result = new byte[array.length + amount];
        System.arraycopy(array, 0, result, 0, array.length);
        for (int i = array.length; i < result.length; i++) {
            result[i] = 0;
        }
        return result;
    }

    protected final byte[] intToBytes(int input) {
        return new byte[] {
                (byte) (input >>> 24 & 0xFF),
                (byte) (input >>> 16 & 0xFF),
                (byte) (input >>> 8 & 0xFF),
                (byte) (input & 0xFF)
        };
    }

    protected final byte[] trim(byte[] arr) {
        int begin = 0, end = arr.length;
        for (int i = 0; i < arr.length; i++) { // find the first non-null byte{
            if (arr[i] != 0) {
                begin = i;
                break;
            }
        }
        for (int i = arr.length - 1; i >= 0; i--) { //find the last non-null byte
            if (arr[i] != 0) {
                end = i;
                break;
            }
        }
        return subarray(arr, begin, end);
    }

    protected final byte[] subarray(byte[] in, int a, int b) {
        if (b - a > in.length) {
            return in;
        }
        byte[] out = new byte[(b - a) + 1];
        if (b + 1 - a >= 0) {
            System.arraycopy(in, a, out, 0, b + 1 - a);
        }
        return out;
    }

    protected final byte[][] split(byte[] input) {
        ArrayList<byte[]> temp = new ArrayList<>();
        int index_cache = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] == 0x00) {
                byte[] b = subarray(input, index_cache, i - 1);
                temp.add(b);
                index_cache = i + 1;//note, this is the index *after* the null byte
            }
        }
        //get the remaining part
        if (index_cache != 0) { //prevent duplication if there are no null bytes
            byte[] b = subarray(input, index_cache, input.length - 1);
            temp.add(b);
        }
        byte[][] output = new byte[temp.size()][input.length];
        for (int i = 0; i < temp.size(); i++) {
            output[i] = temp.get(i);
        }
        return output;
    }
}