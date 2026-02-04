package cc.restfulmc.api.common.font;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Single provider in a Minecraft-style font definition (bitmap, reference, space, or ttf).
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderDefinition {
    private String type;
    private String id;
    private FilterDefinition filter;
    private String file;
    private Integer ascent;
    private Integer height;
    private List<String> chars;
    private Map<String, Integer> advances;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FilterDefinition {
        private Boolean uniform;
    }
}
