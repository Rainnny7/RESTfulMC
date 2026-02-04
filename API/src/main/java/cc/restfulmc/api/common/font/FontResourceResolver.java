package cc.restfulmc.api.common.font;

import lombok.experimental.UtilityClass;

/**
 * Resolves Minecraft-style font file paths and references to classpath resource paths.
 * minecraft:font/foo.png → /font/textures/foo.png
 * minecraft:include/space → /font/meta/include/space.json
 */
@UtilityClass
public class FontResourceResolver {

    private static final String MINECRAFT_PREFIX = "minecraft:";
    private static final String FONT_PATH_PREFIX = "font/";
    private static final String INCLUDE_PREFIX = "include/";
    private static final String TEXTURES_PREFIX = "/font/textures/";
    private static final String META_INCLUDE_PREFIX = "/font/meta/include/";

    /**
     * Resolve a reference id (e.g. minecraft:include/space) to a classpath path
     * usable with Class.getResourceAsStream() (e.g. /font/meta/include/space.json).
     *
     * @param id the reference id from the font JSON
     * @return classpath path including leading slash, or null if not a recognized reference
     */
    public static String resolveReference(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String path = id.startsWith(MINECRAFT_PREFIX)
                ? id.substring(MINECRAFT_PREFIX.length())
                : id;
        if (!path.startsWith(INCLUDE_PREFIX)) {
            return null;
        }
        String name = path.substring(INCLUDE_PREFIX.length());
        return META_INCLUDE_PREFIX + name + ".json";
    }

    /**
     * Resolve a font file reference (e.g. minecraft:font/ascii.png) to a classpath path
     * usable with Class.getResourceAsStream() (e.g. /font/textures/ascii.png).
     *
     * @param file the file reference from the font JSON
     * @return classpath path including leading slash, or null if not a recognized path
     */
    public static String resolve(String file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String path = file.startsWith(MINECRAFT_PREFIX)
                ? file.substring(MINECRAFT_PREFIX.length())
                : file;
        if (!path.startsWith(FONT_PATH_PREFIX)) {
            return null;
        }
        String relative = path.substring(FONT_PATH_PREFIX.length());
        return TEXTURES_PREFIX + relative;
    }
}
