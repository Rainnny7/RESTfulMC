package me.braydon.mc.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import me.braydon.mc.common.PlayerUtils;
import me.braydon.mc.config.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * A skin for a {@link Player}.
 *
 * @author Braydon
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @Setter @Getter @ToString
public final class Skin {
    public static final Skin DEFAULT_STEVE = new Skin("http://textures.minecraft.net/texture/60a5bd016b3c9a1b9272e4929e30827a67be4ebb219017adbbc4a4d22ebd5b1", Model.DEFAULT);

    /**
     * The texture URL of this skin.
     */
    @NonNull private final String url;

    /**
     * The model of this skin.
     */
    @NonNull private final Model model;

    /**
     * URLs to the parts of this skin.
     */
    @NonNull @SerializedName("parts") private Map<String, String> partUrls = new HashMap<>();

    /**
     * Populate the part URLs for this skin.
     *
     * @param playerUuid the UUID of the player
     * @return the skin
     */
    @NonNull
    public Skin populatePartUrls(@NonNull String playerUuid) {
        for (Part part : Part.values()) {
            partUrls.put(part.name(), AppConfig.INSTANCE.getServerPublicUrl() + "/player/" + part.name().toLowerCase() + "/" + playerUuid + ".png");
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
        return new Skin(jsonObject.get("url").getAsString(), model);
    }

    /**
     * Possible models for a skin.
     */
    public enum Model {
        SLIM, DEFAULT
    }

    /**
     * The part of a skin.
     */
    @AllArgsConstructor @Getter @ToString
    public enum Part {
        HEAD(8, 8, PlayerUtils.SKIN_TEXTURE_SIZE / 8, PlayerUtils.SKIN_TEXTURE_SIZE / 8);

        /**
         * The coordinates of this part.
         */
        private final int x, y;

        /**
         * The size of this part.
         */
        private final int width, height;
    }
}