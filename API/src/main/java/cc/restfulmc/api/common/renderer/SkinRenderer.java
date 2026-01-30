package cc.restfulmc.api.common.renderer;

import cc.restfulmc.api.common.ImageUtils;
import cc.restfulmc.api.model.skin.ISkinPart;
import cc.restfulmc.api.model.skin.Skin;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * A renderer for a {@link ISkinPart}.
 *
 * @param <T> the type of part to render
 * @author Braydon
 */
public abstract class SkinRenderer<T extends ISkinPart> {
    /**
     * Invoke this render to render the
     * given skin part for the provided skin.
     *
     * @param skin     the skin to render the part for
     * @param part     the part to render
     * @param overlays whether to render overlays
     * @param size     the size to scale the skin part to
     * @return the rendered skin part
     */
    @NonNull public abstract BufferedImage render(@NonNull Skin skin, @NonNull T part, boolean overlays, int size);

    /**
     * Get the texture of a part of a skin.
     *
     * @param skin the skin to get the part texture from
     * @param part the part of the skin to get
     * @param size the size to scale the texture to
     * @return the texture of the skin part
     */
    @SneakyThrows
    protected final BufferedImage getVanillaSkinPart(@NonNull Skin skin, @NonNull ISkinPart.Vanilla part, double size) {
        ISkinPart.Vanilla.Coordinates coordinates = part.getCoordinates(); // The coordinates of the part

        // The skin texture is legacy, use legacy coordinates
        if (skin.isLegacy() && part.hasLegacyCoordinates()) {
            coordinates = part.getLegacyCoordinates();
        }
        int width = part.getWidth(); // The width of the part
        if (skin.getModel() == Skin.Model.SLIM && part.isFrontArm()) {
            width--;
        }
        BufferedImage skinImage = ImageIO.read(new ByteArrayInputStream(skin.getSkinImage())); // The skin texture
        BufferedImage partTexture = getSkinPartTexture(skinImage, coordinates.getX(), coordinates.getY(), width, part.getHeight(), size);
        if (coordinates instanceof ISkinPart.Vanilla.LegacyCoordinates legacyCoordinates && legacyCoordinates.isFlipped()) {
            partTexture = ImageUtils.flip(partTexture);
        }
        return partTexture;
    }

    /**
     * Get the texture of a specific part of a skin.
     *
     * @param skinImage the skin image to get the part from
     * @param x         the x position of the part
     * @param y         the y position of the part
     * @param width     the width of the part
     * @param height    the height of the part
     * @param size      the size to scale the part to
     * @return the texture of the skin part
     */
    @SneakyThrows
    private BufferedImage getSkinPartTexture(@NonNull BufferedImage skinImage, int x, int y, int width, int height, double size) {
        // Create a new BufferedImage for the part of the skin texture
        BufferedImage headTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Crop just the part we want based on our x, y, width, and height
        headTexture.getGraphics().drawImage(skinImage, 0, 0, width, height, x, y, x + width, y + height, null);

        // Scale the skin part texture
        if (size > 0D) {
            headTexture = ImageUtils.resize(headTexture, size);
        }
        return headTexture;
    }

    /**
     * Apply an overlay to a texture.
     *
     * @param overlayImage the part to overlay
     */
    protected final void applyOverlay(@NonNull BufferedImage overlayImage) {
        applyOverlay(overlayImage.createGraphics(), overlayImage);
    }

    /**
     * Apply an overlay to a texture.
     *
     * @param graphics     the graphics to overlay on
     * @param overlayImage the part to overlay
     */
    protected final void applyOverlay(@NonNull Graphics2D graphics, @NonNull BufferedImage overlayImage) {
        try {
            graphics.drawImage(overlayImage, 0, 0, null);
            graphics.dispose();
        } catch (Exception ignored) {
            // We can safely ignore this, legacy
            // skins don't have overlays
        }
    }
}