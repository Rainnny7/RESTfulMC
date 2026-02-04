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
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    /**
     * Gets the image data from the URL.
     *
     * @return the image data
     */
    @SneakyThrows
    public static byte[] getImage(String url) {
        HttpResponse<byte[]> response = Constants.HTTP_CLIENT.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() == 200) {
            return response.body();
        }
        return null;
    }

    /**
     * Copies a rectangle from source to dest in 64×64 skin space (coords 0–64), with optional flip.
     * Rectangles are (min(dx1,dx2), min(dy1,dy2)) to (max(dx1,dx2), max(dy1,dy2)); same for source.
     * If dx1 &gt; dx2 or dy1 &gt; dy2, the copy is flipped so the arm/leg is mirrored.
     *
     * @param image   image to modify
     * @param dx1   dest corner x
     * @param dy1   dest corner y
     * @param dx2   dest opposite corner x
     * @param dy2   dest opposite corner y
     * @param sx1   source corner x
     * @param sy1   source corner y
     * @param sx2   source opposite corner x
     * @param sy2   source opposite corner y
     * @param scale scale factor (e.g. 1 for 64×32→64×64)
     */
    public static void copyRect(@NonNull BufferedImage image, int dx1, int dy1, int dx2, int dy2,
                                int sx1, int sy1, int sx2, int sy2, double scale) {
        int dxMin = Math.min(dx1, dx2), dxMax = Math.max(dx1, dx2);
        int dyMin = Math.min(dy1, dy2), dyMax = Math.max(dy1, dy2);
        int sxMin = Math.min(sx1, sx2), sxMax = Math.max(sx1, sx2);
        int syMin = Math.min(sy1, sy2), syMax = Math.max(sy1, sy2);
        int sw = sxMax - sxMin, sh = syMax - syMin;
        int dw = dxMax - dxMin, dh = dyMax - dyMin;
        if (sw != dw || sh != dh) {
            return;
        }
        int sox = (int) (sxMin * scale), soy = (int) (syMin * scale);
        int dox = (int) (dxMin * scale), doy = (int) (dyMin * scale);
        int w = Math.max(1, (int) (sw * scale)), h = Math.max(1, (int) (sh * scale));
        boolean flipX = dx1 > dx2, flipY = dy1 > dy2;

        // Extract source region once
        int[] srcPixels = image.getRGB(sox, soy, w, h, null, 0, w);
        int[] destPixels = new int[w * h];

        // Apply flips in memory
        for (int dy = 0; dy < h; dy++) {
            for (int dx = 0; dx < w; dx++) {
                int srcIdx = (flipY ? h - 1 - dy : dy) * w + (flipX ? w - 1 - dx : dx);
                destPixels[dy * w + dx] = srcPixels[srcIdx];
            }
        }

        // Write back once
        image.setRGB(dox, doy, w, h, destPixels, 0, w);
    }

    /**
     * Sets a rectangular area to fully transparent if the entire area is currently opaque.
     * If any pixel in the area has alpha < 128, no changes are made.
     *
     * @param image   the image to modify in-place
     * @param x1    first corner x-coordinate in skin space
     * @param y1    first corner y-coordinate in skin space
     * @param x2    opposite corner x-coordinate in skin space
     * @param y2    opposite corner y-coordinate in skin space
     * @param scale scale factor applied to coordinates
     */
    public static void setAreaTransparentIfOpaque(@NonNull BufferedImage image, int x1, int y1, int x2, int y2, double scale) {
        int xMin = (int) (Math.min(x1, x2) * scale), xMax = (int) (Math.max(x1, x2) * scale);
        int yMin = (int) (Math.min(y1, y2) * scale), yMax = (int) (Math.max(y1, y2) * scale);
        int w = xMax - xMin, h = yMax - yMin;

        // Get pixels once
        int[] pixels = image.getRGB(xMin, yMin, w, h, null, 0, w);

        // Check for transparency
        boolean hasTransparency = false;
        for (int pixel : pixels) {
            if (((pixel >> 24) & 0xFF) < 128) {
                hasTransparency = true;
                break;
            }
        }
        if (!hasTransparency) {
            // Make transparent
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] &= 0x00_FFFFFF;
            }
            image.setRGB(xMin, yMin, w, h, pixels, 0, w);
        }
    }
}