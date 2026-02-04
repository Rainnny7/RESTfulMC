package cc.restfulmc.api.model.player;

import com.google.gson.JsonObject;
import lombok.*;

/**
 * A cape for a {@link Player}.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Getter @ToString
public final class Cape {
    /**
     * The texture URL of this cape.
     */
    @NonNull private final String url;

    /**
     * Build a cape from the given Json object.
     *
     * @param jsonObject the json object to build from
     * @return the built cape
     */
    public static Cape fromJsonObject(JsonObject jsonObject) {
        if (jsonObject == null) { // No object to parse
            return null;
        }
        return new Cape(jsonObject.get("url").getAsString());
    }
}