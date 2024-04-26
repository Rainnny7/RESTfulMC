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
package cc.restfulmc.sdk.response.server;

import cc.restfulmc.sdk.response.CacheableResponse;
import cc.restfulmc.sdk.response.server.dns.DNSRecord;
import lombok.*;

import java.util.UUID;

/**
 * A representation of a Minecraft server.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public abstract class MinecraftServer extends CacheableResponse {
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
     * The DNS records resolved for this server, null if none.
     */
    private final DNSRecord[] records;

    /**
     * The Geo location of this server, null if unknown.
     */
    private GeoLocation geo;

    /**
     * The player counts of this server.
     */
    @NonNull private final Players players;

    /**
     * The MOTD of this server.
     */
    @NonNull private final MOTD motd;

    /**
     * The Geo location of a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class GeoLocation {
        /**
         * The continent of this server.
         */
        @NonNull private final LocationData continent;

        /**
         * The country of this server.
         */
        @NonNull private final LocationData country;

        /**
         * The city of this server, null if unknown.
         */
        private final String city;

        /**
         * The latitude of this server.
         */
        private final double latitude;

        /**
         * The longitude of this server.
         */
        private final double longitude;

        /**
         * Data for a location.
         */
        @AllArgsConstructor @Getter @ToString
        public static class LocationData {
            /**
             * The location code.
             */
            @NonNull private final String code;

            /**
             * The location name.
             */
            @NonNull private final String name;
        }
    }

    /**
     * Player count data for a server.
     */
    @AllArgsConstructor @Getter @ToString
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
            @NonNull private final Name name;

            /**
             * The name of a sample player.
             */
            @AllArgsConstructor @Getter @ToString
            public static class Name {
                /**
                 * The raw name.
                 */
                @NonNull private final String raw;

                /**
                 * The clean name (no color codes).
                 */
                @NonNull private final String clean;

                /**
                 * The HTML name.
                 */
                @NonNull private final String html;
            }
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
    }

    /**
     * The platform of a Minecraft server.
     */
    @AllArgsConstructor @Getter
    public enum Platform {
        JAVA(JavaMinecraftServer.class),
        BEDROCK(BedrockMinecraftServer.class);

        /**
         * The server class this platform represents.
         */
        @NonNull private final Class<? extends MinecraftServer> serverClass;
    }
}