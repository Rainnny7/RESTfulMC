package cc.restfulmc.api.common;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * @author Braydon
 */
@UtilityClass
public final class ImageUtils {
    /**
     * Scale the given image to the provided size.
     *
     * @param image the image to scale
     * @param size  the size to scale the image to
     * @return the scaled image
     */
    @NonNull
    public static BufferedImage resize(@NonNull BufferedImage image, double size) {
        BufferedImage scaled = new BufferedImage((int) (image.getWidth() * size), (int) (image.getHeight() * size), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        graphics.drawImage(image, AffineTransform.getScaleInstance(size, size), null);
        graphics.dispose();
        return scaled;
    }

    /**
     * Flip the given image.
     *
     * @param image the image to flip
     * @return the flipped image
     */
    @NonNull
    public static BufferedImage flip(@NonNull BufferedImage image) {
        BufferedImage flipped = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = flipped.createGraphics();
        graphics.drawImage(image, image.getWidth(), 0, 0, image.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
        graphics.dispose();
        return flipped;
    }

    /**
     * Get the byte array from the given image.
     *
     * @param image the image to extract from
     * @return the byte array of the image
     */
    @SneakyThrows
    public static byte[] toByteArray(@NonNull BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        }
    }

    /**
     * Convert a base64 string to an image.
     *
     * @param base64 the base64 string to convert
     * @return the image
     */
    @SneakyThrows
    public static BufferedImage base64ToImage(String base64) {
        String favicon = base64.contains("data:image/png;base64,") ? base64.split(",", 2)[1] : base64;

        // Strip whitespace (newlines, spaces) - some Minecraft servers send favicon with line breaks
        favicon = favicon.replaceAll("\\s+", "");
        try {
            return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(favicon)));
        } catch (Exception e) {
            throw new Exception("Base64 could not be converted to image", e);
        }
    }
}