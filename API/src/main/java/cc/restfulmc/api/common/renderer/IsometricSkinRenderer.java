package cc.restfulmc.api.common.renderer;

import cc.restfulmc.api.model.skin.ISkinPart;
import lombok.NonNull;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A isometric renderer for a {@link ISkinPart}.
 *
 * @param <T> the type of part to render
 * @author Braydon
 */
public abstract class IsometricSkinRenderer<T extends ISkinPart> extends SkinRenderer<T> {
    /**
     * Draw a part onto the texture.
     *
     * @param graphics  the graphics to draw to
     * @param partImage the part image to draw
     * @param transform the transform to apply
     * @param x         the x position to draw at
     * @param y         the y position to draw at
     * @param width     the part image width
     * @param height    the part image height
     */
    protected final void drawPart(@NonNull Graphics2D graphics, @NonNull BufferedImage partImage, @NonNull AffineTransform transform,
                          double x, double y, int width, int height) {
        graphics.setTransform(transform);
        graphics.drawImage(partImage, (int) x, (int) y, width, height, null);
    }
}