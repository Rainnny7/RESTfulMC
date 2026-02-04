package cc.restfulmc.api.common.color;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Utilities for working with Minecraft color codes.
 *
 * @author Braydon
 */
@UtilityClass
public final class ColorUtils {
    /**
     * Pattern to match all color code formats.
     */
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]|§x(§[0-9A-F]){6}|§#[0-9A-Fa-f]{6}");

    /**
     * Strips the color codes from the given input.
     *
     * @param input the input to strip
     * @return the stripped input
     */
    @NonNull
    public static String stripColor(@NonNull String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Converts the given input into HTML format.
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
        String color = null;
        String activeColor = null;
        int format = 0;
        int activeFormat = 0; // Bitmask: 1=bold, 2=italic, 4=underline, 8=strikethrough
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
                        if (chars[idx] == '§') {
                            hex.append(chars[idx + 1]);
                        } else {
                            valid = false;
                        }
                    }
                    if (valid) {
                        color = hex.toString();
                        i += 13;
                        continue;
                    }
                }
                if (code == 'r') {
                    color = null;
                    format = 0;
                    i++;
                    continue;
                }
                MinecraftColor mcColor = MinecraftColor.getByCode(code);
                if (mcColor != null) {
                    color = mcColor.toHex();
                    i++;
                    continue;
                }
                if (code == 'l') {
                    format |= 1;
                    i++;
                    continue;
                }
                if (code == 'o') {
                    format |= 2;
                    i++;
                    continue;
                }
                if (code == 'n') {
                    format |= 4;
                    i++;
                    continue;
                }
                if (code == 'm') {
                    format |= 8;
                    i++;
                    continue;
                }
                if (code == 'k') {
                    i++;
                    continue;
                }
            }

            // Check if style changed
            boolean sameStyle = Objects.equals(color, activeColor) && format == activeFormat;
            if (!sameStyle) {
                if (!pending.isEmpty()) {
                    if (hasSpan) {
                        result.append(buildSpan(activeColor, activeFormat, pending.toString()));
                    } else {
                        result.append(pending);
                    }
                    pending.setLength(0);
                }
                activeColor = color;
                activeFormat = format;
                hasSpan = activeColor != null || activeFormat != 0;
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
            if (hasSpan) {
                result.append(buildSpan(activeColor, activeFormat, pending.toString()));
            } else {
                result.append(pending);
            }
        }
        return result.toString();
    }

    /**
     * Builds an HTML span with the given color and format.
     *
     * @param color the hex color, or null
     * @param format the format bitmask
     * @param content the span content
     * @return the HTML span
     */
    private static String buildSpan(String color, int format, @NonNull String content) {
        StringBuilder style = new StringBuilder();
        if (color != null) {
            style.append("color:").append(color);
        }
        if ((format & 1) != 0) {
            style.append(style.isEmpty() ? "" : ";").append("font-weight:bold");
        }
        if ((format & 2) != 0) {
            style.append(style.isEmpty() ? "" : ";").append("font-style:italic");
        }
        if ((format & 12) != 0) {
            style.append(style.isEmpty() ? "" : ";").append("text-decoration:");
            style.append((format & 4) != 0 && (format & 8) != 0 ? "underline line-through" : (format & 4) != 0 ? "underline" : "line-through");
        }
        return "<span style=\"" + style + "\">" + content + "</span>";
    }

    /**
     * Parses a hex color at the given index.
     * <p>
     * Tries §x§R§R§G§G§B§B first (14 chars), then §#RRGGBB (7 chars).
     * Returns the parsed color and characters consumed, or null if neither format is present.
     * </p>
     *
     * @param line the string containing the color code
     * @param index the index of the leading § character
     * @return the parsed result (color + chars consumed) if valid, null otherwise
     */
    public static HexColorResult parseHexColor(@NonNull String line, int index) {
        Color extendedColor = parseHexColorX(line, index);
        if (extendedColor != null) {
            return new HexColorResult(extendedColor, 14);
        }
        Color sharpColor = parseSharpHexColor(line, index);
        if (sharpColor != null) {
            return new HexColorResult(sharpColor, 7);
        }
        return null;
    }

    /**
     * Parses §x§R§R§G§G§B§B at the given index.
     *
     * @param line the string containing the color code
     * @param index the index of the leading § character
     * @return the Color if valid, null otherwise
     */
    public static Color parseHexColorX(@NonNull String line, int index) {
        if (index + 14 > line.length() || line.charAt(index) != '§' || Character.toLowerCase(line.charAt(index + 1)) != 'x') {
            return null;
        }
        StringBuilder hex = new StringBuilder("#");
        for (int j = 0; j < 6; j++) {
            int idx = index + 2 + (j * 2);
            if (idx + 1 >= line.length() || line.charAt(idx) != '§') {
                return null;
            }
            char hexDigit = line.charAt(idx + 1);
            if ((hexDigit >= '0' && hexDigit <= '9') || (hexDigit >= 'A' && hexDigit <= 'F') || (hexDigit >= 'a' && hexDigit <= 'f')) {
                hex.append(hexDigit);
            } else {
                return null;
            }
        }
        try {
            return Color.decode(hex.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses §#RRGGBB at the given index.
     *
     * @param line the string containing the color code
     * @param index the index of the leading § character
     * @return the Color if valid, null otherwise
     */
    public static Color parseSharpHexColor(@NonNull String line, int index) {
        if (index + 8 > line.length() || line.charAt(index) != '§' || line.charAt(index + 1) != '#') {
            return null;
        }
        String hex = line.substring(index + 2, index + 8);
        if (!hex.matches("[0-9A-Fa-f]{6}")) {
            return null;
        }
        try {
            return Color.decode("#" + hex);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
