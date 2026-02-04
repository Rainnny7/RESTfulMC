package cc.restfulmc.api.model.player;

import cc.restfulmc.api.common.ImageUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import lombok.*;

import javax.imageio.ImageIO;

/**
 * A cape for a {@link Player}.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Getter @ToString
public final class Cape {
    /**
     * The ID of this cape.
     */
    @NonNull @JsonIgnore private final String id;

    /**
     * The texture URL of this cape.
     */
    @NonNull private final String url;

    /**
     * The bytes to the cape image its self.
     */
    @JsonIgnore private final byte[] capeBytes;

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
        String url = jsonObject.get("url").getAsString();
        String[] urlSplit = url.split("/");
        return new Cape(urlSplit[urlSplit.length - 1], url, ImageUtils.getImage(url));
    }
}