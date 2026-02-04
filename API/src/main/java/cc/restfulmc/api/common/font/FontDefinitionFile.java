package cc.restfulmc.api.common.font;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Minecraft-style font definition root (fonts.json / default.json).
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FontDefinitionFile {
    private List<ProviderDefinition> providers;
}
