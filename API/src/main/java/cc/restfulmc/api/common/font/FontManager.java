package cc.restfulmc.api.common.font;

import cc.restfulmc.api.RESTfulMC;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads Minecraft-style font definitions from meta JSON
 * and exposes texture-based BitmapFonts.
 *
 * @author Braydon
 */
@Log4j2
public final class FontManager {
    private static final String META_PREFIX = "/font/meta/";
    private static final String WIDTH_PREFIX = "/font/meta/width/";
    private static final String DEFAULT_META = "default";
    private static final String DEFAULT_WIDTHS_PATH = "/font/default_widths.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * The singleton instance.
     */
    private static FontManager instance;

    /**
     * Loaded fonts by name.
     */
    private final Map<String, BitmapFont> fonts = new ConcurrentHashMap<>();

    /**
     * Gets the singleton instance.
     *
     * @return the font manager instance
     */
    @NonNull
    public static synchronized FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    /**
     * Loads the default font. Idempotent after first successful load.
     */
    public synchronized void load() {
        loadFont(DEFAULT_META);
    }

    /**
     * Loads a font by name (default, alt, uniform). Idempotent after first successful load.
     *
     * @param name the font name
     * @return the loaded font, or null if loading failed
     */
    public synchronized BitmapFont loadFont(@NonNull String name) {
        if (fonts.containsKey(name)) {
            return fonts.get(name);
        }
        String metaPath = META_PREFIX + name + ".json";
        String widthsPath = WIDTH_PREFIX + "minecraft_" + name + ".json";
        FontWidthsFile widthsFile = loadWidths(widthsPath);
        if (widthsFile == null) {
            widthsFile = loadWidths(DEFAULT_WIDTHS_PATH);
        }
        boolean isUniform = "uniform".equals(name);
        BitmapFont font = buildFont(metaPath, widthsFile, isUniform, new HashSet<>());
        if (font != null) {
            fonts.put(name, font);
            log.info("Loaded bitmap font '{}' from {} (widths: {})",
                    name, metaPath, widthsFile != null ? "Minecraft" : "default");
        }
        return font;
    }

    /**
     * Builds a font from a meta definition file.
     *
     * @param metaPath the path to the meta JSON
     * @param widthsFile the widths file, or null
     * @param isUniform whether uniform mode is enabled
     * @param visitedRefs visited references for cycle detection
     * @return the built font, or null if building failed
     */
    private BitmapFont buildFont(@NonNull String metaPath, FontWidthsFile widthsFile,
                                 boolean isUniform, @NonNull Set<String> visitedRefs) {
        try (InputStream in = RESTfulMC.class.getResourceAsStream(metaPath)) {
            if (in == null) {
                log.error("Font meta not found: {}", metaPath);
                return null;
            }
            FontDefinitionFile def = MAPPER.readValue(in, FontDefinitionFile.class);
            List<ProviderDefinition> providers = def.getProviders();
            if (providers == null || providers.isEmpty()) {
                log.warn("No providers in font meta: {}", metaPath);
                return null;
            }
            int ascent = 7;
            int height = 8;
            int defaultGlyphWidth = widthsFile != null ? widthsFile.getMissingCharWidth() : 8;
            BitmapFont font = new BitmapFont(ascent, height, defaultGlyphWidth);
            if (widthsFile != null && widthsFile.getMissingChar() != null) {
                font.setDefaultBoldOffset(widthsFile.getMissingChar().getBoldOffset());
            }
            processProviders(providers, font, widthsFile, isUniform, visitedRefs);
            return font;
        } catch (Exception e) {
            log.error("Failed to load font meta: {}", metaPath, e);
            return null;
        }
    }

    /**
     * Processes font providers and populates the font.
     *
     * @param providers the provider definitions
     * @param font the font to populate
     * @param widthsFile the widths file, or null
     * @param isUniform whether uniform mode is enabled
     * @param visitedRefs visited references for cycle detection
     */
    private void processProviders(@NonNull List<ProviderDefinition> providers, @NonNull BitmapFont font,
                                  FontWidthsFile widthsFile, boolean isUniform, @NonNull Set<String> visitedRefs) {
        for (ProviderDefinition provider : providers) {
            String type = provider.getType();
            if (type == null) {
                continue;
            }

            switch (type.toLowerCase()) {
                case "reference" -> processReference(provider, font, widthsFile, isUniform, visitedRefs);
                case "bitmap" -> processBitmap(provider, font, widthsFile);
                case "space" -> processSpace(provider, font, widthsFile);
                default -> { /* skip unsupported */ }
            }
        }
    }

    /**
     * Processes a reference provider.
     *
     * @param provider the provider definition
     * @param font the font to populate
     * @param widthsFile the widths file, or null
     * @param isUniform whether uniform mode is enabled
     * @param visitedRefs visited references for cycle detection
     */
    private void processReference(@NonNull ProviderDefinition provider, @NonNull BitmapFont font,
                                  FontWidthsFile widthsFile, boolean isUniform, @NonNull Set<String> visitedRefs) {
        String id = provider.getId();
        if (id == null || id.isEmpty()) {
            log.warn("Reference provider missing id");
            return;
        }
        if (visitedRefs.contains(id)) {
            log.warn("Circular reference: {}", id);
            return;
        }
        ProviderDefinition.FilterDefinition filter = provider.getFilter();
        if (filter != null && filter.getUniform() != null && !filter.getUniform().equals(isUniform)) {
            return;
        }
        String refPath = FontResourceResolver.resolveReference(id);
        if (refPath == null) {
            log.warn("Could not resolve reference: {}", id);
            return;
        }
        visitedRefs.add(id);
        try (InputStream refIn = RESTfulMC.class.getResourceAsStream(refPath)) {
            if (refIn == null) {
                log.warn("Referenced file not found: {}", refPath);
                return;
            }
            FontDefinitionFile refDef = MAPPER.readValue(refIn, FontDefinitionFile.class);
            List<ProviderDefinition> refProviders = refDef.getProviders();
            if (refProviders != null && !refProviders.isEmpty()) {
                processProviders(refProviders, font, widthsFile, isUniform, visitedRefs);
            }
        } catch (Exception e) {
            log.warn("Failed to load reference {}: {}", id, e.getMessage());
        } finally {
            visitedRefs.remove(id);
        }
    }

    /**
     * Processes a bitmap provider.
     *
     * @param provider the provider definition
     * @param font the font to populate
     * @param widthsFile the widths file, or null
     */
    private void processBitmap(@NonNull ProviderDefinition provider, @NonNull BitmapFont font, FontWidthsFile widthsFile) {
        String path = FontResourceResolver.resolve(provider.getFile());
        if (path == null) {
            log.warn("Could not resolve font file: {}", provider.getFile());
            return;
        }
        try (InputStream imgIn = RESTfulMC.class.getResourceAsStream(path)) {
            if (imgIn == null) {
                log.warn("Font texture not found: {}", path);
                return;
            }
            BufferedImage texture = ImageIO.read(imgIn);
            if (texture == null) {
                log.warn("Failed to read font texture: {}", path);
                return;
            }
            List<String> chars = provider.getChars();
            if (chars == null || chars.isEmpty()) {
                return;
            }
            int rows = chars.size();
            int cols = chars.getFirst().length();
            int cellWidth = texture.getWidth() / cols;
            int cellHeight = texture.getHeight() / rows;
            int providerAscent = provider.getAscent() != null ? provider.getAscent() : 7;
            for (int row = 0; row < rows; row++) {
                String line = chars.get(row);
                for (int col = 0, charIndex = 0; col < cols && charIndex < line.length(); col++) {
                    int codepoint = line.codePointAt(charIndex);
                    if (codepoint != 0) {
                        int srcX = col * cellWidth;
                        int srcY = row * cellHeight;
                        int advance = getAdvance(widthsFile, texture, srcX, srcY, cellWidth, cellHeight, codepoint);
                        double boldOffset = 1.0;
                        double shadowOffset = 1.0;
                        if (widthsFile != null) {
                            FontWidthsFile.CharWidthEntry entry = widthsFile.getCharWidthEntry(codepoint);
                            if (entry != null) {
                                boldOffset = entry.getBoldOffset();
                                shadowOffset = entry.getShadowOffset();
                            }
                        }
                        Glyph glyph = new Glyph(texture, srcX, srcY, cellWidth, cellHeight, advance, boldOffset, shadowOffset, providerAscent);
                        font.putGlyph(codepoint, glyph);
                    }
                    charIndex += Character.charCount(codepoint);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to process bitmap provider: {}", e.getMessage());
        }
    }

    /**
     * Processes a space provider.
     *
     * @param provider the provider definition
     * @param font the font to populate
     * @param widthsFile the widths file, or null
     */
    private void processSpace(@NonNull ProviderDefinition provider, @NonNull BitmapFont font, FontWidthsFile widthsFile) {
        Map<String, Integer> advances = provider.getAdvances();
        if (advances == null) {
            return;
        }
        for (Map.Entry<String, Integer> entry : advances.entrySet()) {
            String key = entry.getKey();
            if (key == null || key.isEmpty()) {
                continue;
            }
            int codepoint = key.codePointAt(0);
            font.putAdvance(codepoint, entry.getValue());
            if (widthsFile != null) {
                FontWidthsFile.CharWidthEntry widthEntry = widthsFile.getCharWidthEntry(codepoint);
                if (widthEntry != null) {
                    font.putBoldOffset(codepoint, widthEntry.getBoldOffset());
                }
            }
        }
    }

    /**
     * Loads a widths file from classpath.
     *
     * @param path the classpath path
     * @return the widths file, or null if not found
     */
    private static FontWidthsFile loadWidths(@NonNull String path) {
        try (InputStream in = RESTfulMC.class.getResourceAsStream(path)) {
            if (in != null) {
                return MAPPER.readValue(in, FontWidthsFile.class);
            }
        } catch (Exception e) {
            // optional
        }
        return null;
    }

    /**
     * Gets the advance for a codepoint from widths or measures from texture.
     *
     * @param widths the widths file, or null
     * @param texture the texture image
     * @param srcX the source X coordinate
     * @param srcY the source Y coordinate
     * @param cellWidth the cell width
     * @param cellHeight the cell height
     * @param codepoint the unicode codepoint
     * @return the advance width
     */
    private static int getAdvance(FontWidthsFile widths, @NonNull BufferedImage texture,
                                  int srcX, int srcY, int cellWidth, int cellHeight, int codepoint) {
        if (widths != null) {
            int width = widths.getAdvance(codepoint);
            if (width >= 0) {
                return width;
            }
        }
        return Glyph.measureAdvance(texture, srcX, srcY, cellWidth, cellHeight);
    }

    /**
     * Gets the default font, loading it if necessary.
     *
     * @return the default font
     */
    @NonNull
    public BitmapFont getDefaultFont() {
        load();
        return Objects.requireNonNull(fonts.get(DEFAULT_META), "Font failed to load");
    }
}
