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
package cc.restfulmc.api.model;

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import lombok.*;
import cc.restfulmc.api.common.ColorUtils;
import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.token.JavaServerStatusToken;
import cc.restfulmc.api.service.pinger.MinecraftServerPinger;
import cc.restfulmc.api.service.pinger.impl.BedrockMinecraftServerPinger;
import cc.restfulmc.api.service.pinger.impl.JavaMinecraftServerPinger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A model representing a Minecraft server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
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
         * Create new geo location data
         * from the given city response.
         *
         * @param geo the geo city response
         * @return the geo location
         */
        @NonNull
        public static GeoLocation create(@NonNull CityResponse geo) {
            Continent continent = geo.getContinent();
            Country country = geo.getCountry();
            City city = geo.getCity();
            Location location = geo.getLocation();
            return new GeoLocation(
                    new LocationData(continent.getCode(), continent.getName()),
                    new LocationData(country.getIsoCode(), country.getName()),
                    city == null ? null : city.getName(),
                    location.getLatitude(), location.getLongitude()
            );
        }

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
         * Create new player count data from a token.
         *
         * @param token the token to create from
         * @return the player count data
         */
        @NonNull
        public static Players create(@NonNull JavaServerStatusToken.Players token) {
            List<Sample> samples = null;
            if (token.getSample() != null) {
                samples = new ArrayList<>(); // The player samples
                for (JavaServerStatusToken.Players.Sample sample : token.getSample()) {
                    samples.add(new Sample(sample.getId(), Sample.Name.create(sample.getName())));
                }
            }
            return new Players(token.getOnline(), token.getMax(), samples != null ? samples.toArray(new Sample[0]) : null);
        }

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

                /**
                 * Create a new name from a raw string.
                 *
                 * @param raw the raw name string
                 * @return the new name
                 */
                @NonNull
                public static Name create(@NonNull String raw) {
                    return new Name(raw, ColorUtils.stripColor(raw), ColorUtils.toHTML(raw));
                }
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