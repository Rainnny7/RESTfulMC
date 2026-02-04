package cc.restfulmc.api.common.font;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Single provider in a Minecraft-style font definition
 * (bitmap, reference, space, or ttf).
 *
 * @author Braydon
 */
@Setter @Getter @JsonIgnoreProperties(ignoreUnknown = true)
public final class ProviderDefinition {
    /**
     * The provider type (bitmap, reference, space, ttf).
     */
    private String type;

    /**
     * The provider id for reference types.
     */
    private String id;

    /**
     * The filter definition for conditional loading.
     */
    private FilterDefinition filter;

    /**
     * The texture file path for bitmap providers.
     */
    private String file;

    /**
     * The ascent value for bitmap providers.
     */
    private Integer ascent;

    /**
     * The height value for bitmap providers.
     */
    private Integer height;

    /**
     * The character rows for bitmap providers.
     */
    private List<String> chars;

    /**
     * The character advances for space providers.
     */
    private Map<String, Integer> advances;

    /**
     * Filter definition for conditional provider loading.
     *
     * @author Braydon
     */
    @Setter @Getter @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class FilterDefinition {
        /**
         * Whether to apply for uniform font mode.
         */
        private Boolean uniform;
    }
}
