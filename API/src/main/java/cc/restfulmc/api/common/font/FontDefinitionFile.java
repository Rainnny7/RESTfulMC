package cc.restfulmc.api.common.font;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Minecraft-style font definition root (fonts.json / default.json).
 *
 * @author Braydon
 */
@Setter @Getter @JsonIgnoreProperties(ignoreUnknown = true)
public final class FontDefinitionFile {
    /**
     * The list of font providers.
     */
    private List<ProviderDefinition> providers;
}
