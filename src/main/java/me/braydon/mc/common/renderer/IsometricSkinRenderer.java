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
package me.braydon.mc.common.renderer;

import lombok.NonNull;
import me.braydon.mc.model.skin.ISkinPart;

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