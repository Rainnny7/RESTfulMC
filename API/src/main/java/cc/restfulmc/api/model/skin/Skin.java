package cc.restfulmc.api.model.skin;

import cc.restfulmc.api.common.ImageUtils;
import cc.restfulmc.api.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A skin for a {@link Player}.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Getter @ToString
public final class Skin {
    /**
     * The texture URL of this skin.
     */
    @NonNull private final String url;

    /**
     * The model of this skin.
     */
    @NonNull private final Model model;

    /**
     * The image data of this skin.
     */
    @JsonIgnore private final byte[] skinImage;

    /**
     * Is this skin legacy?
     */
    private final boolean legacy;

    /**
     * URLs to the parts of this skin.
     */
    @NonNull @JsonProperty("parts") private final Map<String, String> partUrls;

    /**
     * Build a skin from the given Json object.
     *
     * @param jsonObject the json object to build from
     * @return the built skin
     */
    @SneakyThrows
    public static Skin fromJsonObject(JsonObject jsonObject) {
        if (jsonObject == null) { // No object to parse
            return null;
        }
        JsonObject metadataJsonObject = jsonObject.getAsJsonObject("metadata");
        String url = jsonObject.get("url").getAsString();
        BufferedImage image = ImageIO.read(new URL(url)); // Get the skin image
        byte[] bytes = ImageUtils.toByteArray(image); // Convert the image into bytes
        boolean legacy = image.getWidth() == 64 && image.getHeight() == 32; // Is the skin legacy?

        return new Skin(url, metadataJsonObject == null ? Model.DEFAULT : Model.valueOf(metadataJsonObject.get("model").getAsString().toUpperCase()), bytes, legacy, new HashMap<>());
    }

    /**
     * Possible models for a skin.
     */
    public enum Model {
        DEFAULT, SLIM
    }
}