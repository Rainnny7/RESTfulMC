package cc.restfulmc.api.model.server;

import com.maxmind.geoip2.model.AbstractCountryResponse;
import com.maxmind.geoip2.model.CityResponse;
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
     * Create new geo location data
     * from the given geo response.
     *
     * @param geo the geo response
     * @return the geo location, null if unknown
     */
    public static GeoLocation create(@NonNull AbstractCountryResponse geo) {
        Continent continent = geo.getContinent();
        Country country = geo.getCountry();
        City city = null;
        Location location = null;
        if (geo instanceof CityResponse cityResponse) {
            city = cityResponse.getCity();
            location = cityResponse.getLocation();
        }
        if (continent.getCode() == null) {
            return null;
        }
        return new GeoLocation(
                new GeoLocation.LocationData(continent.getCode(), continent.getName()),
                new GeoLocation.LocationData(country.getIsoCode(), country.getName()),
                city == null ? null : city.getName(),
                "https://flagcdn.com/w20/" + country.getIsoCode().toLowerCase() + ".webp",
                location == null ? 0D : location.getLatitude(), location == null ? 0D : location.getLongitude()
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