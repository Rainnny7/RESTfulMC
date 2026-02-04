package cc.restfulmc.api.common.font;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Minecraft font widths (mc-fonts format). Optional resource for exact Minecraft advance values.
 * See <a href="https://github.com/Owen1212055/mc-fonts">mc-fonts</a>.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FontWidthsFile {

    @JsonProperty("missing_char")
    private CharWidthEntry missingChar;

    @JsonProperty("chars")
    private Map<String, CharWidthEntry> chars;

    /**
     * Per-character width entry (Minecraft uses width for advance; bold_offset for bold advance).
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CharWidthEntry {
        private int width;
        @JsonProperty("bold_offset")
        private double boldOffset = 1;
        @JsonProperty("shadow_offset")
        private double shadowOffset = 1;
    }

    public int getAdvance(int codepoint) {
        CharWidthEntry e = getCharWidthEntry(codepoint);
        return e != null ? e.getWidth() : -1;
    }

    public CharWidthEntry getCharWidthEntry(int codepoint) {
        if (chars == null) return null;
        String key = Character.toString(codepoint);
        CharWidthEntry e = chars.get(key);
        if (e != null) return e;
        if (Character.isSupplementaryCodePoint(codepoint)) {
            key = new String(Character.toChars(codepoint));
            return chars.get(key);
        }
        return null;
    }

    public int getMissingCharWidth() {
        return missingChar != null ? missingChar.getWidth() : 6;
    }
}
