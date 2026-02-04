package cc.restfulmc.api.common.font;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
@AllArgsConstructor @Getter
public final class Glyph {
    /**
     * The texture containing this glyph.
     */
    @NonNull private final BufferedImage texture;

    /**
     * The source X coordinate in the texture.
     */
    private final int srcX;

    /**
     * The source Y coordinate in the texture.
     */
    private final int srcY;

    /**
     * The glyph width.
     */
    private final int width;

    /**
     * The glyph height.
     */
    private final int height;

    /**
     * The horizontal advance.
     */
    private final int advance;

    /**
     * The bold offset for bold rendering.
     */
    private final double boldOffset;

    /**
     * The shadow offset for shadow rendering.
     */
    private final double shadowOffset;

    /**
     * The vertical baseline offset.
     */
    private final int ascent;

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
