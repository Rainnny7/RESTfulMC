package cc.restfulmc.api.common.color;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;

/**
 * Minecraft chat formatting colors.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public enum MinecraftColor {
    BLACK('0', 0),
    DARK_BLUE('1', 170),
    DARK_GREEN('2', 43520),
    DARK_AQUA('3', 43690),
    DARK_RED('4', 11141120),
    DARK_PURPLE('5', 11141290),
    GOLD('6', 16755200),
    GRAY('7', 11184810),
    DARK_GRAY('8', 5592405),
    BLUE('9', 5592575),
    GREEN('a', 5635925),
    AQUA('b', 5636095),
    RED('c', 16733525),
    LIGHT_PURPLE('d', 16733695),
    YELLOW('e', 16777045),
    WHITE('f', 16777215);

    /**
     * The color code character.
     */
    private final char code;

    /**
     * The RGB value.
     */
    private final int rgb;

    /**
     * Converts this color to an AWT Color.
     *
     * @return the AWT color
     */
    @NonNull
    public Color toAwtColor() {
        return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    /**
     * Converts this color to a hex string.
     *
     * @return the hex string (e.g. "#FFFFFF")
     */
    @NonNull
    public String toHex() {
        return String.format("#%06X", rgb);
    }

    /**
     * Gets a MinecraftColor by its code character.
     *
     * @param code the color code character
     * @return the color, or null if not found
     */
    public static MinecraftColor getByCode(char code) {
        char lowerCode = Character.toLowerCase(code);
        for (MinecraftColor color : values()) {
            if (color.code == lowerCode) {
                return color;
            }
        }
        return null;
    }
}
