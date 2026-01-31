package cc.restfulmc.api.model.server;

import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
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
     * @param location the geo location location
     * @return the geo location, null if unknown
     */
    public static GeoLocation create(@NonNull Continent continent, @NonNull Country country, City city, Location location) {
        if (continent.code() == null) {
            return null;
        }
        return new GeoLocation(
                new GeoLocation.LocationData(continent.code(), continent.name()),
                new GeoLocation.LocationData(country.isoCode(), country.name()),
                city == null ? null : city.name(),
                "https://flagcdn.com/w20/" + country.isoCode().toLowerCase() + ".webp",
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