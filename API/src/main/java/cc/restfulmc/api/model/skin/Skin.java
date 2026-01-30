package cc.restfulmc.api.model.skin;

import cc.restfulmc.api.common.ImageUtils;
import cc.restfulmc.api.config.AppConfig;
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
    public static final Skin DEFAULT_STEVE = create("http://textures.minecraft.net/texture/60a5bd016b3c9a1b9272e4929e30827a67be4ebb219017adbbc4a4d22ebd5b1", Model.DEFAULT);

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
     * Populate the part URLs for this skin.
     *
     * @param playerUuid the UUID of the player
     * @return the skin
     */
    @NonNull
    public Skin populatePartUrls(@NonNull String playerUuid) {
        for (Enum<?>[] type : ISkinPart.TYPES) {
            for (Enum<?> part : type) {
                partUrls.put(part.name(), AppConfig.INSTANCE.getServerPublicUrl() + "/player/" + part.name().toLowerCase() + "/" + playerUuid + ".png");
            }
        }
        return this;
    }

    /**
     * Build a skin from the given Json object.
     *
     * @param jsonObject the json object to build from
     * @return the built skin
     */
    public static Skin fromJsonObject(JsonObject jsonObject) {
        if (jsonObject == null) { // No object to parse
            return null;
        }
        Model model = Model.DEFAULT; // The skin model

        JsonObject metadataJsonObject = jsonObject.getAsJsonObject("metadata");
        if (metadataJsonObject != null) { // Parse the skin model
            model = Model.valueOf(metadataJsonObject.get("model").getAsString().toUpperCase());
        }
        return create(jsonObject.get("url").getAsString(), model);
    }

    /**
     * Create a skin from the given URL and model.
     *
     * @param url the skin url
     * @param model the skin model
     * @return the constructed skin
     */
    @NonNull @SneakyThrows
    private static Skin create(@NonNull String url, @NonNull Model model) {
        BufferedImage image = ImageIO.read(new URL(url)); // Get the skin image
        byte[] bytes = ImageUtils.toByteArray(image); // Convert the image into bytes
        boolean legacy = image.getWidth() == 64 && image.getHeight() == 32; // Is the skin legacy?
        return new Skin(url, model, bytes, legacy, new HashMap<>());
    }

    /**
     * Possible models for a skin.
     */
    public enum Model {
        DEFAULT, SLIM
    }
}