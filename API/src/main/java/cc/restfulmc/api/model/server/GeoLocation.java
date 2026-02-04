package cc.restfulmc.api.model.server;

import com.maxmind.geoip2.record.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The Geo location of a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class GeoLocation {
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
     * The region of this server.
     */
    private final String region;

    /**
     * The IANA time zone (e.g. America/Toronto) of this server, null if unknown.
     */
    private final String timezone;

    /**
     * The postal code of this server, null if unknown.
     */
    private final String postal;

    /**
     * A quick link to the flag for the country.
     */
    @NonNull private final String flag;

    /**
     * The latitude of this server.
     */
    private final double latitude;

    /**
     * The longitude of this server.
     */
    private final double longitude;

    /**
     * Create new geo location data.
     *
     * @param continent the geo location continent
     * @param country the geo location country
     * @param city the geo location city
     * @param subdivision the subdivision
     * @param postal the postal code
     * @param location the geo location location
     * @return the geo location, null if unknown
     */
    public static GeoLocation create(@NonNull Continent continent, @NonNull Country country, City city, Subdivision subdivision, Postal postal, Location location) {
        String continentCode = continent.code();
        if (continentCode == null) {
            return null;
        }
        String isoCode = country.isoCode();
        return new GeoLocation(
                new GeoLocation.LocationData(continentCode, continent.name()),
                new GeoLocation.LocationData(isoCode, country.name()),
                city == null ? null : city.name(),
                subdivision == null ? null : subdivision.name(),
                location == null ? null : location.timeZone(),
                postal == null ? null : postal.code(),
                "https://flagcdn.com/w20/" + isoCode.toLowerCase() + ".webp",
                location == null ? 0D : location.latitude(), location == null ? 0D : location.longitude()
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