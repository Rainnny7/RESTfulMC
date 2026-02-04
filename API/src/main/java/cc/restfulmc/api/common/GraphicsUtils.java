package cc.restfulmc.api.common;

import cc.restfulmc.api.common.font.BitmapFont;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Braydon
 */
@UtilityClass
public class GraphicsUtils {
    /**
     * Draws a string using the given bitmap font and returns the x position after the last character.
     */
    public static int drawString(Graphics2D g, BitmapFont font, String str, int x, int y) {
        return drawString(g, font, str, x, y, 1);
    }

    /**
     * Draws a string at the given scale (without mutating the font). When scale != 1, applies a transform
     * so the font renders at native 1x and is scaled up. Returns the x position after the last character.
     */
    public static int drawString(Graphics2D g, BitmapFont font, String str, int x, int y, int scale) {
        if (str == null || str.isEmpty()) return x;
        if (scale == 1) {
            font.drawString(g, str, x, y);
            return x + font.stringWidth(str);
        }
        AffineTransform saved = g.getTransform();
        g.translate(x, y);
        g.scale(scale, scale);
        font.drawString(g, str, 0, 0);
        g.setTransform(saved);
        return x + font.stringWidth(str) * scale;
    }

    /**
     * Draws a string with Minecraft-style style options: shadow (dark offset pass), bold (double-draw +1px),
     * italic (shear transform). Returns the x position after the last character (advance = stringWidth + 1 if bold).
     */
    public static int drawStringWithStyle(Graphics2D g, BitmapFont font, String str, int x, int y,
                                         boolean shadow, boolean bold, boolean italic) {
        return drawStringWithStyle(g, font, str, x, y, shadow, bold, italic, 1);
    }

    /**
     * Draws a string with style at the given scale (without mutating the font). When scale != 1, applies
     * a transform so the font renders at native 1x and is scaled up.
     */
    public static int drawStringWithStyle(Graphics2D g, BitmapFont font, String str, int x, int y,
                                          boolean shadow, boolean bold, boolean italic, int scale) {
        if (str == null || str.isEmpty()) return x;
        AffineTransform savedTransform = g.getTransform();
        int drawX = x;
        int drawY = y;
        if (scale != 1) {
            g.translate(x, y);
            g.scale(scale, scale);
            drawX = 0;
            drawY = 0;
        }
        if (italic) {
            g.shear(-0.2, 0);
        }
        Color savedColor = g.getColor();
        if (shadow) {
            g.setColor(new Color(
                (int) (savedColor.getRed() * 0.25f),
                (int) (savedColor.getGreen() * 0.25f),
                (int) (savedColor.getBlue() * 0.25f),
                savedColor.getAlpha()
            ));
            font.drawString(g, str, drawX + 1, drawY + 1, bold);
            if (bold) {
                font.drawString(g, str, drawX + 2, drawY + 1, true);  // Shadow for bold copy
            }
            g.setColor(savedColor);
        }
        font.drawString(g, str, drawX, drawY, bold);
        if (bold) {
            font.drawString(g, str, drawX + 1, drawY, true);
        }
        g.setTransform(savedTransform);
        int advance = font.stringWidth(str, bold);
        return x + advance * scale;
    }

    /**
     * Returns the width of the string when drawn at the given scale.
     */
    public static int stringWidthAtScale(BitmapFont font, String str, int scale) {
        return font.stringWidth(str, false) * scale;
    }
}
