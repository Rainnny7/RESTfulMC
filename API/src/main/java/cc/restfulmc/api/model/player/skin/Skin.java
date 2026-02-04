package cc.restfulmc.api.model.player.skin;

import cc.restfulmc.api.config.AppConfig;
import cc.restfulmc.api.model.player.Player;
import cc.restfulmc.api.service.PlayerService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A skin for a {@link Player}.
 *
 * @author Braydon
 */
@Getter @ToString
public final class Skin {
    public static final Skin DEFAULT_STEVE = create("https://textures.minecraft.net/texture/60a5bd016b3c9a1b9272e4929e30827a67be4ebb219017adbbc4a4d22ebd5b1", Model.DEFAULT);

    /**
     * The texture URL of this skin.
     */
    @NonNull private final String url;

    /**
     * The model of this skin.
     */
    @NonNull private final Model model;

    /**
     * The raw image for this skin.
     */
    @JsonIgnore @Transient private final BufferedImage image;

    /**
     * The image data of this skin.
     */
    @JsonIgnore private final byte[] skinImage;

    /**
     * Is this skin legacy?
     */
    @Transient private final boolean legacy;

    /**
     * URLs to the parts of this skin.
     */
    @NonNull @JsonProperty("parts") private final Map<String, String> partUrls;

    @PersistenceCreator @SneakyThrows
    private Skin(@NonNull String url, @NonNull Model model, byte[] skinImage, @NonNull Map<String, String> partUrls) {
        this.url = url;
        this.model = model;
        this.skinImage = skinImage;
        this.partUrls = partUrls;

        // Load the image its self from the bytes and determine whether the skin is legacy
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(skinImage)) {
            image = ImageIO.read(inputStream);
            legacy = image.getWidth() == 64 && image.getHeight() == 32;
        }
    }

    /**
     * Populate the part URLs for this skin.
     *
     * @param playerUuid the UUID of the player
     * @return the skin
     */
    @NonNull
    public Skin populatePartUrls(@NonNull String playerUuid) {
        for (SkinRendererType rendererType : SkinRendererType.values()) {
            partUrls.put(rendererType.name(), "%s/player/%s/%s.png".formatted(
                    AppConfig.INSTANCE.getServerPublicUrl(), playerUuid,rendererType.name().toLowerCase()
            ));
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
        JsonObject metadataJsonObject = jsonObject.getAsJsonObject("metadata");
        Model model = metadataJsonObject == null ? Model.DEFAULT : Model.valueOf(metadataJsonObject.get("model").getAsString().toUpperCase());
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
        return new Skin(url, model, PlayerService.INSTANCE.getSkinTexture(url, true), new HashMap<>());
    }

    /**
     * Possible models for a skin.
     */
    public enum Model {
        DEFAULT, SLIM
    }
}