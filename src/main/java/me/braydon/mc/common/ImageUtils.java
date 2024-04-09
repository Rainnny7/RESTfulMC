/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny), and the RESTfulMC contributors
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
package me.braydon.mc.common;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.braydon.mc.model.Skin;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * @author Braydon
 */
@UtilityClass
public final class ImageUtils {
    public static final int SKIN_TEXTURE_SIZE = 64; // The skin of a skin texture

    /**
     * Get the texture of a part of a skin.
     *
     * @param skin the skin to get the part texture from
     * @param part the part of the skin to get
     * @param size the size to scale the texture to
     * @return the texture of the skin part
     */
    @SneakyThrows
    public static byte[] getSkinPart(@NonNull Skin skin, @NonNull Skin.Part part, int size) {
        return getSkinPartTexture(skin, part.getX(), part.getY(), part.getWidth(), part.getHeight(), size);
    }

    /**
     * Get the texture of a specific part of a skin.
     *
     * @param skin the skin to get the part from
     * @param x the x position of the part
     * @param y the y position of the part
     * @param width the width of the part
     * @param height the height of the part
     * @param size the size to scale the part to
     * @return the texture of the skin part
     */
    @SneakyThrows
    public static byte[] getSkinPartTexture(@NonNull Skin skin, int x, int y, int width, int height, int size) {
        BufferedImage skinImage = ImageIO.read(new URL(skin.getUrl())); // The skin texture

        // Create a new BufferedImage for the part of the skin texture
        BufferedImage headTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Crop just the part we want based on our x, y, width, and height
        headTexture.getGraphics().drawImage(skinImage, 0, 0, width, height, x, y, x + width, y + height, null);

        // Scale the skin part texture
        double scale = (double) size / width;
        AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
        headTexture = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(headTexture, null);

        // Convert BufferedImage to byte array
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(headTexture, "png", outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        }
    }
}