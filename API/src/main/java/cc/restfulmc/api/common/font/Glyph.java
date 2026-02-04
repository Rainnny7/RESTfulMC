package cc.restfulmc.api.common.font;

import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * A single glyph: reference to texture, source rectangle,
 * horizontal advance, and style offsets (Minecraft-style).
 * <p>
 * Advance is the distance to move after drawing this glyph;
 * it can be less than width for narrow characters.
 * boldOffset and shadowOffset default to 1 when not specified.
 * ascent is the vertical baseline offset for this glyph
 * (different providers have different ascents).
 * </p>
 *
 * @author Braydon
 */
public record Glyph(@NonNull BufferedImage texture, int srcX, int srcY, int width, int height,
                    int advance, double boldOffset, double shadowOffset, int ascent) {
    /**
     * Gets the advance when bold: base advance + boldOffset (Minecraft getAdvance(bold)).
     *
     * @param bold whether bold style is applied
     * @return the advance width
     */
    public int getAdvance(boolean bold) {
        return bold ? advance + (int) Math.ceil(boldOffset) : advance;
    }

    /**
     * Measures the horizontal advance like Minecraft's bitmap provider:
     * rightmost column with any non-transparent pixel plus one
     * (no extra spacing; use default_widths.json for exact Minecraft values).
     *
     * @param texture the texture image
     * @param srcX the source X coordinate
     * @param srcY the source Y coordinate
     * @param width the glyph width
     * @param height the glyph height
     * @return the measured advance
     */
    public static int measureAdvance(@NonNull BufferedImage texture, int srcX, int srcY, int width, int height) {
        int rightmost = -1;
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                int alpha = (texture.getRGB(srcX + col, srcY + row) >> 24) & 0xff;
                if (alpha > 0) {
                    rightmost = col;
                    break;
                }
            }
        }
        return rightmost >= 0 ? rightmost + 1 : width;
    }
}
