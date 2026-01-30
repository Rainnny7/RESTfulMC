package cc.restfulmc.api.common.renderer.impl;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.model.skin.ISkinPart;
import cc.restfulmc.api.model.skin.Skin;
import lombok.NonNull;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A basic 2D renderer for a {@link ISkinPart.Vanilla}.
 *
 * @author Braydon
 */
public final class VanillaSkinPartRenderer extends SkinRenderer<ISkinPart.Vanilla> {
    public static final VanillaSkinPartRenderer INSTANCE = new VanillaSkinPartRenderer();

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
    @Override @NonNull
    public BufferedImage render(@NonNull Skin skin, @NonNull ISkinPart.Vanilla part, boolean overlays, int size) {
        double scale = size / 8D;
        BufferedImage partImage = getVanillaSkinPart(skin, part, scale); // Get the part image
        if (!overlays) { // Not rendering overlays
            return partImage;
        }
        // Create a new image, draw our skin part texture, and then apply overlays
        BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB); // The texture to return
        Graphics2D graphics = texture.createGraphics(); // Create the graphics for drawing
        graphics.drawImage(partImage, 0, 0, null);

        // Draw part overlays
        ISkinPart.Vanilla[] overlayParts = part.getOverlays();
        if (overlayParts != null) {
            for (ISkinPart.Vanilla overlay : overlayParts) {
                applyOverlay(graphics, getVanillaSkinPart(skin, overlay, scale));
            }
        }
        graphics.dispose();
        return texture;
    }
}