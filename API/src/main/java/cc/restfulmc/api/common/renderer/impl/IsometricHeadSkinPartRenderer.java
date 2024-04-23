/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cc.restfulmc.api.common.renderer.impl;

import lombok.NonNull;
import cc.restfulmc.api.common.renderer.IsometricSkinRenderer;
import cc.restfulmc.api.model.skin.ISkinPart;
import cc.restfulmc.api.model.skin.Skin;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A isometric 3D renderer for a {@link ISkinPart.Custom#HEAD}.
 *
 * @author Braydon
 */
public final class IsometricHeadSkinPartRenderer extends IsometricSkinRenderer<ISkinPart.Custom> {
    public static final IsometricHeadSkinPartRenderer INSTANCE = new IsometricHeadSkinPartRenderer();

    private static final double SKEW_A = 26D / 45D;   // 0.57777777
    private static final double SKEW_B = SKEW_A * 2D; // 1.15555555

    private static final AffineTransform HEAD_TOP_TRANSFORM = new AffineTransform(1D, -SKEW_A, 1, SKEW_A, 0, 0);
    private static final AffineTransform FACE_TRANSFORM = new AffineTransform(1D, -SKEW_A, 0D, SKEW_B, 0d, SKEW_A);
    private static final AffineTransform HEAD_LEFT_TRANSFORM = new AffineTransform(1D, SKEW_A, 0D, SKEW_B, 0D, 0D);

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
    public BufferedImage render(@NonNull Skin skin, @NonNull ISkinPart.Custom part, boolean overlays, int size) {
        double scale = (size / 8D) / 2.5;
        double zOffset = scale * 3.5D;
        double xOffset = scale * 2D;

        BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB); // The texture to return
        Graphics2D graphics = texture.createGraphics(); // Create the graphics for drawing

        // Get the Vanilla skin parts to draw
        BufferedImage headTop = getVanillaSkinPart(skin, ISkinPart.Vanilla.HEAD_TOP, scale);
        BufferedImage face = getVanillaSkinPart(skin, ISkinPart.Vanilla.FACE, scale);
        BufferedImage headLeft = getVanillaSkinPart(skin, ISkinPart.Vanilla.HEAD_LEFT, scale);

        // Draw the top head part
        drawPart(graphics, headTop, HEAD_TOP_TRANSFORM, -0.5 - zOffset, xOffset + zOffset, headTop.getWidth(), headTop.getHeight() + 2);

        // Draw the face part
        double x = xOffset + 8 * scale;
        drawPart(graphics, face, FACE_TRANSFORM, x, x + zOffset - 0.5, face.getWidth(), face.getHeight());

        // Draw the left head part
        drawPart(graphics, headLeft, HEAD_LEFT_TRANSFORM, xOffset + 1, zOffset - 0.5, headLeft.getWidth(), headLeft.getHeight());

        graphics.dispose();
        return texture;
    }
}