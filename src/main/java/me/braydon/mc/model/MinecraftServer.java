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
package me.braydon.mc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import me.braydon.mc.common.ColorUtils;
import me.braydon.mc.service.pinger.MinecraftServerPinger;
import me.braydon.mc.service.pinger.impl.BedrockMinecraftServerPinger;
import me.braydon.mc.service.pinger.impl.JavaMinecraftServerPinger;

import java.util.Arrays;
import java.util.UUID;

/**
 * A model representing a Minecraft server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class MinecraftServer {
    /**
     * The hostname of this server.
     */
    @EqualsAndHashCode.Include @NonNull private final String hostname;

    /**
     * The IP address of this server, if resolved.
     */
    private final String ip;

    /**
     * The port of this server.
     */
    @EqualsAndHashCode.Include private final int port;

    /**
     * The player counts of this server.
     */
    @NonNull private final Players players;

    /**
     * The MOTD of this server.
     */
    @NonNull private final MOTD motd;

    /**
     * Player count data for a server.
     */
    @AllArgsConstructor @Getter @ToString @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Players {
        /**
         * The online players on this server.
         */
        private final int online;

        /**
         * The maximum allowed players on this server.
         */
        private final int max;

        /**
         * A sample of players on this server, null or empty if no sample.
         */
        private final Sample[] sample;

        /**
         * A sample player.
         */
        @AllArgsConstructor @Getter @ToString
        public static class Sample {
            /**
             * The unique id of this player.
             */
            @NonNull private final UUID id;

            /**
             * The name of this player.
             */
            @NonNull private final String name;
        }
    }

    /**
     * The MOTD for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class MOTD {
        /**
         * The raw MOTD lines.
         */
        @NonNull private final String[] raw;

        /**
         * The clean MOTD lines (no color codes).
         */
        @NonNull private final String[] clean;

        /**
         * The HTML MOTD lines.
         */
        @NonNull private final String[] html;

        /**
         * Create a new MOTD from a raw string.
         *
         * @param raw the raw motd string
         * @return the new motd
         */
        @NonNull
        public static MOTD create(@NonNull String raw) {
            String[] rawLines = raw.split("\n"); // The raw lines
            return new MOTD(
                    rawLines,
                    Arrays.stream(rawLines).map(ColorUtils::stripColor).toArray(String[]::new),
                    Arrays.stream(rawLines).map(ColorUtils::toHTML).toArray(String[]::new)
            );
        }
    }

    /**
     * A platform a Minecraft
     * server can operate on.
     */
    @AllArgsConstructor @Getter
    public enum Platform {
        /**
         * The Java edition of Minecraft.
         */
        JAVA(new JavaMinecraftServerPinger(), 25565),

        /**
         * The Bedrock edition of Minecraft.
         */
        BEDROCK(new BedrockMinecraftServerPinger(), 19132);

        /**
         * The server pinger for this platform.
         */
        @NonNull private final MinecraftServerPinger<?> pinger;

        /**
         * The default server port for this platform.
         */
        private final int defaultPort;
    }
}