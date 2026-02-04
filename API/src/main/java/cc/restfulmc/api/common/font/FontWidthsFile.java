package cc.restfulmc.api.common.font;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Minecraft font widths (mc-fonts format).
 * <p>
 * Optional resource for exact Minecraft advance values.
 * See <a href="https://github.com/Owen1212055/mc-fonts">mc-fonts</a>.
 * </p>
 *
 * @author Braydon
 */
@Setter @Getter @JsonIgnoreProperties(ignoreUnknown = true)
public final class FontWidthsFile {
    /**
     * The entry for missing characters.
     */
    @JsonProperty("missing_char") private CharWidthEntry missingChar;

    /**
     * Character width entries mapped by character string.
     */
    @JsonProperty("chars") private Map<String, CharWidthEntry> chars;

    /**
     * Gets the advance width for a codepoint.
     *
     * @param codepoint the unicode codepoint
     * @return the advance width, or -1 if not found
     */
    public int getAdvance(int codepoint) {
        CharWidthEntry entry = getCharWidthEntry(codepoint);
        return entry != null ? entry.getWidth() : -1;
    }

    /**
     * Gets the character width entry for a codepoint.
     *
     * @param codepoint the unicode codepoint
     * @return the entry, or null if not found
     */
    public CharWidthEntry getCharWidthEntry(int codepoint) {
        if (chars == null) {
            return null;
        }
        String key = Character.toString(codepoint);
        CharWidthEntry entry = chars.get(key);
        if (entry != null) {
            return entry;
        }
        if (Character.isSupplementaryCodePoint(codepoint)) {
            key = new String(Character.toChars(codepoint));
            return chars.get(key);
        }
        return null;
    }

    /**
     * Gets the width for missing characters.
     *
     * @return the missing character width
     */
    public int getMissingCharWidth() {
        return missingChar != null ? missingChar.getWidth() : 6;
    }

    /**
     * Per-character width entry.
     * <p>
     * Minecraft uses width for advance; bold_offset for bold advance.
     * </p>
     *
     * @author Braydon
     */
    @Setter @Getter @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class CharWidthEntry {
        /**
         * The character width.
         */
        private int width;

        /**
         * The bold offset for bold rendering.
         */
        @JsonProperty("bold_offset") private double boldOffset = 1;

        /**
         * The shadow offset for shadow rendering.
         */
        @JsonProperty("shadow_offset") private double shadowOffset = 1;
    }
}
