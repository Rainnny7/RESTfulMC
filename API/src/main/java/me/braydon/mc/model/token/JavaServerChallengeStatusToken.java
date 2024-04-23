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
package me.braydon.mc.model.token;

import lombok.*;
import me.braydon.mc.model.server.JavaMinecraftServer;

import java.util.HashMap;
import java.util.Map;

/**
 * A token representing the response from
 * sending a challenge request via UDP to
 * a {@link JavaMinecraftServer} using the
 * query.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Getter @ToString
public final class JavaServerChallengeStatusToken {
    /**
     * The map (world) of this server.
     */
    @NonNull private final String map;

    /**
     * The plugins of this server.
     */
    private final Map<String, String> plugins;

    /**
     * Create a new challenge token
     * from the given raw data.
     *
     * @param rawData the raw data
     * @return the challenge token
     */
    @NonNull
    public static JavaServerChallengeStatusToken create(@NonNull Map<String, String> rawData) {
        Map<String, String> plugins = new HashMap<>();
        for (String plugin : rawData.get("plugins").split(": ")[1].split("; ")) {
            String[] split = plugin.split(" ");
            plugins.put(split[0], split[1]);
        }
        return new JavaServerChallengeStatusToken(rawData.get("map"), plugins);
    }
}