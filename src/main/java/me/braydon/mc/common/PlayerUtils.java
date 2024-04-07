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
public final class PlayerUtils {
    private static final int SKIN_TEXTURE_SIZE = 64; // The skin of a skin texture

    /**
     * Get the head texture of a skin.
     *
     * @param skin the skin to get the head texture from
     * @param size the size to scale the head texture to
     * @return the head texture of the skin
     */
    @SneakyThrows
    public static byte[] getHeadTexture(@NonNull Skin skin, int size) {
        return getSkinPartTexture(skin, 8, 8, SKIN_TEXTURE_SIZE / 8, SKIN_TEXTURE_SIZE / 8, size);
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