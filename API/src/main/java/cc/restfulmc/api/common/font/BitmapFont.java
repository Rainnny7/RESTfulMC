package cc.restfulmc.api.common.font;

import lombok.NonNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Texture-based font: draws and measures text by blitting
 * glyph regions from loaded textures.
 *
 * @author Braydon
 */
public final class BitmapFont {
    /**
     * Map of codepoints to their glyphs.
     */
    private final Map<Integer, Glyph> glyphs = new HashMap<>();

    /**
     * Map of codepoints to advance overrides.
     */
    private final Map<Integer, Integer> advanceOverrides = new HashMap<>();

    /**
     * Bold offset for advance-only characters (no glyph), from widths file.
     */
    private final Map<Integer, Double> advanceOnlyBoldOffsets = new HashMap<>();

    /**
     * The font ascent (baseline offset from top).
     */
    private final int ascent;

    /**
     * The font line height.
     */
    private final int height;

    /**
     * The default glyph width for missing characters.
     */
    private final int defaultGlyphWidth;

    /**
     * Default bold offset when character has no entry in widths file (e.g. from missing_char).
     */
    private double defaultBoldOffset = 1.0;

    public BitmapFont(int ascent, int height, int defaultGlyphWidth) {
        this.ascent = ascent;
        this.height = height;
        this.defaultGlyphWidth = defaultGlyphWidth;
    }

    /**
     * Sets the default bold offset (e.g. from widths file missing_char).
     *
     * @param defaultBoldOffset the default bold offset
     */
    void setDefaultBoldOffset(double defaultBoldOffset) {
        this.defaultBoldOffset = defaultBoldOffset;
    }

    /**
     * Registers a glyph for a codepoint.
     *
     * @param codepoint the unicode codepoint
     * @param glyph the glyph to register
     */
    void putGlyph(int codepoint, @NonNull Glyph glyph) {
        glyphs.put(codepoint, glyph);
    }

    /**
     * Registers an advance override for a codepoint.
     *
     * @param codepoint the unicode codepoint
     * @param advance the advance width
     */
    void putAdvance(int codepoint, int advance) {
        advanceOverrides.put(codepoint, advance);
    }

    /**
     * Sets the bold offset for an advance-only character (from widths file).
     *
     * @param codepoint the unicode codepoint
     * @param boldOffset the bold offset
     */
    void putBoldOffset(int codepoint, double boldOffset) {
        advanceOnlyBoldOffsets.put(codepoint, boldOffset);
    }

    /**
     * Gets the font ascent.
     *
     * @return the ascent
     */
    public int ascent() {
        return ascent;
    }

    /**
     * Gets the font line height.
     *
     * @return the height
     */
    public int height() {
        return height;
    }

    /**
     * Calculates the total width of the string in pixels.
     * <p>
     * Uses per-glyph advance like Minecraft, not fixed cell width.
     * </p>
     *
     * @param str the string to measure
     * @return the width in pixels
     */
    public int stringWidth(String str) {
        return stringWidth(str, false);
    }

    /**
     * Calculates the total width when drawn with bold.
     * <p>
     * Advance = width + boldOffset per character when bold.
     * </p>
     *
     * @param str the string to measure
     * @param bold whether bold style is applied
     * @return the width in pixels
     */
    public int stringWidth(String str, boolean bold) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < str.length(); ) {
            int codepoint = str.codePointAt(i);
            total += getAdvance(codepoint, bold);
            i += Character.charCount(codepoint);
        }
        return total;
    }

    /**
     * Gets the advance for a single codepoint.
     * <p>
     * Uses glyph metrics when available, else advanceOverride, else defaultGlyphWidth.
     * When bold, adds bold offset from widths file for advance-only characters,
     * or 1 if not in widths file.
     * </p>
     *
     * @param codepoint the unicode codepoint
     * @param bold whether bold style is applied
     * @return the advance width
     */
    public int getAdvance(int codepoint, boolean bold) {
        Glyph glyph = glyphs.get(codepoint);
        if (glyph != null) {
            return glyph.getAdvance(bold);
        }
        Integer override = advanceOverrides.get(codepoint);
        int base = override != null ? override : defaultGlyphWidth;
        if (!bold) {
            return base;
        }
        double offset = advanceOnlyBoldOffsets.getOrDefault(codepoint, defaultBoldOffset);
        return base + (int) Math.ceil(offset);
    }

    /**
     * Draws the string with baseline at (x, y).
     * <p>
     * Glyphs are tinted by the current Graphics2D color (Minecraft-style).
     * Advance-only chars (e.g. space) are not drawn but advance the cursor.
     * Uses per-glyph ascent for vertical positioning - glyph top is at y - glyph.ascent().
     * </p>
     *
     * @param graphics the graphics context
     * @param str the string to draw
     * @param x the x coordinate
     * @param y the y coordinate (baseline)
     */
    public void drawString(@NonNull Graphics2D graphics, String str, int x, int y) {
        drawString(graphics, str, x, y, false);
    }

    /**
     * Draws the string with optional bold.
     * <p>
     * When bold, advances by getAdvance(cp, true) so spacing matches
     * stringWidth(str, bold) and Minecraft-style bold (draw at x and x+1) lines up correctly.
     * </p>
     *
     * @param graphics the graphics context
     * @param str the string to draw
     * @param x the x coordinate
     * @param y the y coordinate (baseline)
     * @param bold whether bold style is applied
     */
    public void drawString(@NonNull Graphics2D graphics, String str, int x, int y, boolean bold) {
        if (str == null || str.isEmpty()) {
            return;
        }
        Color color = graphics.getColor();
        for (int i = 0; i < str.length(); ) {
            int codepoint = str.codePointAt(i);
            Glyph glyph = glyphs.get(codepoint);
            if (glyph != null) {
                int glyphDrawY = y - glyph.getAscent();
                drawGlyphTinted(graphics, glyph, x, glyphDrawY, color);
            }
            x += getAdvance(codepoint, bold);
            i += Character.charCount(codepoint);
        }
    }

    /**
     * Draws a single glyph tinted by the given color
     * (glyph alpha as mask, color for visible pixels).
     *
     * @param graphics the graphics context
     * @param glyph the glyph to draw
     * @param x the x coordinate
     * @param y the y coordinate
     * @param color the tint color
     */
    private void drawGlyphTinted(@NonNull Graphics2D graphics, @NonNull Glyph glyph, int x, int y, @NonNull Color color) {
        BufferedImage src = glyph.getTexture();
        int srcX = glyph.getSrcX();
        int srcY = glyph.getSrcY();
        int glyphWidth = glyph.getWidth();
        int glyphHeight = glyph.getHeight();
        BufferedImage tinted = new BufferedImage(glyphWidth, glyphHeight, BufferedImage.TYPE_INT_ARGB);
        int colorRed = color.getRed();
        int colorGreen = color.getGreen();
        int colorBlue = color.getBlue();
        for (int py = 0; py < glyphHeight; py++) {
            for (int px = 0; px < glyphWidth; px++) {
                int argb = src.getRGB(srcX + px, srcY + py);
                int alpha = (argb >> 24) & 0xff;
                int srcRed = (argb >> 16) & 0xff;
                int srcGreen = (argb >> 8) & 0xff;
                int srcBlue = argb & 0xff;
                int tintedRed = (srcRed * colorRed) / 255;
                int tintedGreen = (srcGreen * colorGreen) / 255;
                int tintedBlue = (srcBlue * colorBlue) / 255;
                tinted.setRGB(px, py, (alpha << 24) | (tintedRed << 16) | (tintedGreen << 8) | tintedBlue);
            }
        }
        graphics.drawImage(tinted, x, y, null);
    }
}
