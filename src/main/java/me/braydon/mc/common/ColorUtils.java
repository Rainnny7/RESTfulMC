package me.braydon.mc.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Braydon
 */
@UtilityClass
public final class ColorUtils {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Map<Character, String> COLOR_MAP = new HashMap<>();
    static {
        // Map each color to its corresponding hex code
        COLOR_MAP.put('0', "#000000"); // Black
        COLOR_MAP.put('1', "#0000AA"); // Dark Blue
        COLOR_MAP.put('2', "#00AA00"); // Dark Green
        COLOR_MAP.put('3', "#00AAAA"); // Dark Aqua
        COLOR_MAP.put('4', "#AA0000"); // Dark Red
        COLOR_MAP.put('5', "#AA00AA"); // Dark Purple
        COLOR_MAP.put('6', "#FFAA00"); // Gold
        COLOR_MAP.put('7', "#AAAAAA"); // Gray
        COLOR_MAP.put('8', "#555555"); // Dark Gray
        COLOR_MAP.put('9', "#5555FF"); // Blue
        COLOR_MAP.put('a', "#55FF55"); // Green
        COLOR_MAP.put('b', "#55FFFF"); // Aqua
        COLOR_MAP.put('c', "#FF5555"); // Red
        COLOR_MAP.put('d', "#FF55FF"); // Light Purple
        COLOR_MAP.put('e', "#FFFF55"); // Yellow
        COLOR_MAP.put('f', "#FFFFFF"); // White
    }

    /**
     * Strip the color codes
     * from the given input.
     *
     * @param input the input to strip
     * @return the stripped input
     */
    @NonNull
    public static String stripColor(@NonNull String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Convert the given input into HTML format.
     * <p>
     * This will replace each color code with
     * a span tag with the respective color in
     * hex format.
     * </p>
     *
     * @param input the input to convert
     * @return the converted input
     */
    @NonNull
    public static String toHTML(@NonNull String input) {
        StringBuilder builder = new StringBuilder();
        boolean nextIsColor = false; // Is the next char a color code?

        for (char character : input.toCharArray()) {
            // Found color symbol, next color is the color
            if (character == '§') {
                nextIsColor = true;
                continue;
            }
            if (nextIsColor) { // Map the current color to its hex code
                String color = COLOR_MAP.getOrDefault(Character.toLowerCase(character), "");
                builder.append("<span style=\"color:").append(color).append("\">");
                nextIsColor = false;
                continue;
            }
            builder.append(character); // Append the char...
        }
        return builder.toString();
    }
}