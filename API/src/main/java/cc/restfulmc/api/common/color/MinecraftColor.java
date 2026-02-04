package cc.restfulmc.api.common.color;

import lombok.Getter;

import java.awt.*;

/**
 * Minecraft chat formatting colors.
 */
@Getter
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

    private final char code;
    private final int rgb;

    MinecraftColor(char code, int rgb) {
        this.code = code;
        this.rgb = rgb;
    }

    public Color toAwtColor() {
        return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    public String toHex() {
        return String.format("#%06X", rgb);
    }

    public static MinecraftColor getByCode(char code) {
        char c = Character.toLowerCase(code);
        for (MinecraftColor color : values()) {
            if (color.code == c) return color;
        }
        return null;
    }
}
