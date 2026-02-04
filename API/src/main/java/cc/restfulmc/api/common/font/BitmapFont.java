package cc.restfulmc.api.common.font;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Texture-based font: draws and measures text by blitting glyph regions from loaded textures.
 */
public class BitmapFont {

    private final Map<Integer, Glyph> glyphs = new HashMap<>();
    private final Map<Integer, Integer> advanceOverrides = new HashMap<>();
    /** Bold offset for advance-only characters (no glyph), from widths file. */
    private final Map<Integer, Double> advanceOnlyBoldOffsets = new HashMap<>();
    private final int ascent;
    private final int height;
    private final int defaultGlyphWidth;
    /** Default bold offset when character has no entry in widths file (e.g. from missing_char). */
    private double defaultBoldOffset = 1.0;

    public BitmapFont(int ascent, int height, int defaultGlyphWidth) {
        this.ascent = ascent;
        this.height = height;
        this.defaultGlyphWidth = defaultGlyphWidth;
    }

    /** Set default bold offset (e.g. from widths file missing_char). */
    void setDefaultBoldOffset(double defaultBoldOffset) {
        this.defaultBoldOffset = defaultBoldOffset;
    }

    void putGlyph(int codepoint, Glyph glyph) {
        glyphs.put(codepoint, glyph);
    }

    void putAdvance(int codepoint, int advance) {
        advanceOverrides.put(codepoint, advance);
    }

    /** Set bold offset for an advance-only character (from widths file). */
    void putBoldOffset(int codepoint, double boldOffset) {
        advanceOnlyBoldOffsets.put(codepoint, boldOffset);
    }

    public int ascent() {
        return ascent;
    }

    public int height() {
        return height;
    }

    /**
     * Total width of the string in pixels (scaled). Uses per-glyph advance like Minecraft, not fixed cell width.
     */
    public int stringWidth(String str) {
        return stringWidth(str, false);
    }

    /**
     * Total width when drawn with bold. Advance = width + boldOffset per character when bold.
     */
    public int stringWidth(String str, boolean bold) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < str.length(); ) {
            int cp = str.codePointAt(i);
            total += getAdvance(cp, bold);
            i += Character.charCount(cp);
        }
        return total;
    }

    /**
     * Advance for a single codepoint. Uses glyph metrics when available, else advanceOverride, else defaultGlyphWidth.
     * When bold, adds bold offset from widths file for advance-only characters, or 1 if not in widths file.
     */
    public int getAdvance(int codepoint, boolean bold) {
        Glyph g = glyphs.get(codepoint);
        if (g != null) {
            return g.getAdvance(bold);
        }
        Integer override = advanceOverrides.get(codepoint);
        int base = override != null ? override : defaultGlyphWidth;
        if (!bold) return base;
        double offset = advanceOnlyBoldOffsets.getOrDefault(codepoint, defaultBoldOffset);
        return base + (int) Math.ceil(offset);
    }

    /**
     * Draw the string with baseline at (x, y). Glyphs are tinted by the current Graphics2D color (Minecraft-style).
     * Advance-only chars (e.g. space) are not drawn but advance the cursor.
     * Uses per-glyph ascent for vertical positioning - glyph top is at y - glyph.ascent().
     */
    public void drawString(Graphics2D g, String str, int x, int y) {
        drawString(g, str, x, y, false);
    }

    /**
     * Draw the string with optional bold. When bold, advances by getAdvance(cp, true) so spacing matches
     * stringWidth(str, bold) and Minecraft-style bold (draw at x and x+1) lines up correctly.
     */
    public void drawString(Graphics2D g, String str, int x, int y, boolean bold) {
        if (str == null || str.isEmpty()) {
            return;
        }
        Color color = g.getColor();
        for (int i = 0; i < str.length(); ) {
            int cp = str.codePointAt(i);
            Glyph glyph = glyphs.get(cp);
            if (glyph != null) {
                int glyphDrawY = y - glyph.ascent();
                drawGlyphTinted(g, glyph, x, glyphDrawY, color);
            }
            x += getAdvance(cp, bold);
            i += Character.charCount(cp);
        }
    }

    /**
     * Draw a single glyph tinted by the given color (glyph alpha as mask, color for visible pixels).
     */
    private void drawGlyphTinted(Graphics2D g, Glyph glyph, int x, int y, Color color) {
        BufferedImage src = glyph.texture();
        int srcX = glyph.srcX();
        int srcY = glyph.srcY();
        int gw = glyph.width();
        int gh = glyph.height();
        BufferedImage tinted = new BufferedImage(gw, gh, BufferedImage.TYPE_INT_ARGB);
        int cr = color.getRed();
        int cg = color.getGreen();
        int cb = color.getBlue();
        for (int py = 0; py < gh; py++) {
            for (int px = 0; px < gw; px++) {
                int argb = src.getRGB(srcX + px, srcY + py);
                int a = (argb >> 24) & 0xff;
                int sr = (argb >> 16) & 0xff;
                int sg = (argb >> 8) & 0xff;
                int sb = argb & 0xff;
                int tr = (sr * cr) / 255;
                int tg = (sg * cg) / 255;
                int tb = (sb * cb) / 255;
                tinted.setRGB(px, py, (a << 24) | (tr << 16) | (tg << 8) | tb);
            }
        }
        g.drawImage(tinted, x, y, null);
    }
}
