package cc.restfulmc.api.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Braydon
 */
@UtilityClass
public final class ColorUtils {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]|§x(§[0-9A-F]){6}");
    private static final Map<Character, String> COLOR_MAP = new HashMap<>();
    private static final Map<Character, String> FORMAT_MAP = new HashMap<>();
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

        // Map formatting codes to CSS
        FORMAT_MAP.put('l', "font-weight:bold");
        FORMAT_MAP.put('o', "font-style:italic");
        FORMAT_MAP.put('n', "text-decoration:underline");
        FORMAT_MAP.put('m', "text-decoration:line-through");
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
     * hex format. Supports legacy color codes,
     * hex colors (gradients), and formatting codes.
     * </p>
     *
     * @param input the input to convert
     * @return the converted input
     */
    @NonNull
    public static String toHTML(@NonNull String input) {
        StringBuilder result = new StringBuilder();
        StringBuilder pending = new StringBuilder();
        String color = null, activeColor = null;
        int fmt = 0, activeFmt = 0; // Bitmask: 1=bold, 2=italic, 4=underline, 8=strikethrough
        boolean hasSpan = false;

        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '§' && i + 1 < chars.length) {
                char code = Character.toLowerCase(chars[i + 1]);

                // Hex color: §x§R§R§G§G§B§B
                if (code == 'x' && i + 13 < chars.length) {
                    StringBuilder hex = new StringBuilder("#");
                    boolean valid = true;
                    for (int j = 0; j < 6 && valid; j++) {
                        int idx = i + 2 + (j * 2);
                        if (idx + 1 < chars.length && chars[idx] == '§') hex.append(chars[idx + 1]);
                        else valid = false;
                    }
                    if (valid) { color = hex.toString(); i += 13; continue; }
                }
                if (code == 'r') { color = null; fmt = 0; i++; continue; }
                if (COLOR_MAP.containsKey(code)) { color = COLOR_MAP.get(code); i++; continue; }
                if (code == 'l') { fmt |= 1; i++; continue; }
                if (code == 'o') { fmt |= 2; i++; continue; }
                if (code == 'n') { fmt |= 4; i++; continue; }
                if (code == 'm') { fmt |= 8; i++; continue; }
                if (code == 'k') { i++; continue; }
            }

            // Check if style changed
            boolean sameStyle = Objects.equals(color, activeColor) && fmt == activeFmt;
            if (!sameStyle) {
                if (!pending.isEmpty()) {
                    if (hasSpan) result.append(buildSpan(activeColor, activeFmt, pending.toString()));
                    else result.append(pending);
                    pending.setLength(0);
                }
                activeColor = color;
                activeFmt = fmt;
                hasSpan = activeColor != null || activeFmt != 0;
            }

            // Escape and buffer
            pending.append(switch (chars[i]) {
                case ' ' -> "&nbsp;";
                case '<' -> "&lt;";
                case '>' -> "&gt;";
                case '&' -> "&amp;";
                case '"' -> "&quot;";
                default -> chars[i];
            });
        }

        // Flush remaining
        if (!pending.isEmpty()) {
            if (hasSpan) result.append(buildSpan(activeColor, activeFmt, pending.toString()));
            else result.append(pending);
        }
        return result.toString();
    }

    private static String buildSpan(String color, int fmt, String content) {
        StringBuilder style = new StringBuilder();
        if (color != null) style.append("color:").append(color);
        if ((fmt & 1) != 0) style.append(style.isEmpty() ? "" : ";").append("font-weight:bold");
        if ((fmt & 2) != 0) style.append(style.isEmpty() ? "" : ";").append("font-style:italic");
        if ((fmt & 12) != 0) {
            style.append(style.isEmpty() ? "" : ";").append("text-decoration:");
            style.append((fmt & 4) != 0 && (fmt & 8) != 0 ? "underline line-through" : (fmt & 4) != 0 ? "underline" : "line-through");
        }
        return "<span style=\"" + style + "\">" + content + "</span>";
    }
}