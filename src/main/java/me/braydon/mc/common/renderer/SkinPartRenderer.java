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
import lombok.SneakyThrows;
import me.braydon.mc.common.ImageUtils;
import me.braydon.mc.model.Skin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * A renderer for a {@link Skin.Part}.
 *
 * @author Braydon
 * @param <T> the type of part to render
 */
public abstract class SkinPartRenderer<T extends Skin.IPart> {
    /**
     * Invoke this render to render the
     * given skin part for the provided skin.
     *
     * @param skin the skin to render the part for
     * @param part the part to render
     * @param overlays whether to render overlays
     * @param size the size to scale the skin part to
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
    protected BufferedImage getSkinPart(@NonNull Skin skin, @NonNull Skin.Part part, double size) {
        return getSkinPartTexture(skin, part.getCoordinates().getX(), part.getCoordinates().getY(), part.getWidth(), part.getHeight(), size);
    }

    /**
     * Get the texture of a specific part of a skin.
     *
     * @param skin   the skin to get the part from
     * @param x      the x position of the part
     * @param y      the y position of the part
     * @param width  the width of the part
     * @param height the height of the part
     * @param size   the size to scale the part to
     * @return the texture of the skin part
     */
    @SneakyThrows
    private BufferedImage getSkinPartTexture(@NonNull Skin skin, int x, int y, int width, int height, double size) {
        BufferedImage skinImage = ImageIO.read(new URL(skin.getUrl())); // The skin texture

        // Create a new BufferedImage for the part of the skin texture
        BufferedImage headTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Crop just the part we want based on our x, y, width, and height
        headTexture.getGraphics().drawImage(skinImage, 0, 0, width, height, x, y, x + width, y + height, null);

        // Scale the skin part texture
        headTexture = ImageUtils.resize(headTexture, size);

        return headTexture;
    }
}