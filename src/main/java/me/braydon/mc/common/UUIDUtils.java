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
package me.braydon.mc.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * @author Braydon
 */
@UtilityClass
public final class UUIDUtils {
    /**
     * Add dashes to an untrimmed uuid.
     *
     * @param trimmed the untrimmed uuid
     * @return the uuid with dashes
     */
    @NonNull
    public static UUID addDashes(@NonNull String trimmed) {
        StringBuilder builder = new StringBuilder(trimmed);
        for (int i = 0, pos = 20; i < 4; i++, pos -= 4) {
            builder.insert(pos, "-");
        }
        return UUID.fromString(builder.toString());
    }
}