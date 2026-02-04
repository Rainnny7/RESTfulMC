package cc.restfulmc.api.common.font;

import cc.restfulmc.api.RESTfulMC;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads Minecraft-style font definitions from meta JSON and exposes texture-based BitmapFonts.
 */
@Slf4j
public class FontManager {

    private static final String META_PREFIX = "/font/meta/";
    private static final String WIDTH_PREFIX = "/font/meta/width/";
    private static final String DEFAULT_META = "default";
    private static final String DEFAULT_WIDTHS_PATH = "/font/default_widths.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static FontManager instance;
    private final Map<String, BitmapFont> fonts = new ConcurrentHashMap<>();

    public static synchronized FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    /**
     * Load font from meta and build BitmapFont. Idempotent after first successful load per name.
     */
    public synchronized void load() {
        loadFont(DEFAULT_META);
    }

    /**
     * Load font by name (default, alt, uniform). Idempotent after first successful load.
     */
    public synchronized BitmapFont loadFont(String name) {
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

    private BitmapFont buildFont(String metaPath, FontWidthsFile widthsFile,
                                 boolean isUniform, Set<String> visitedRefs) {
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

    private void processProviders(List<ProviderDefinition> providers, BitmapFont font,
                                  FontWidthsFile widthsFile, boolean isUniform, Set<String> visitedRefs) {
        for (ProviderDefinition provider : providers) {
            String type = provider.getType();
            if (type == null) continue;

            switch (type.toLowerCase()) {
                case "reference" -> processReference(provider, font, widthsFile, isUniform, visitedRefs);
                case "bitmap" -> processBitmap(provider, font, widthsFile);
                case "space" -> processSpace(provider, font, widthsFile);
                default -> { /* skip unsupported */ }
            }
        }
    }

    private void processReference(ProviderDefinition provider, BitmapFont font,
                                  FontWidthsFile widthsFile, boolean isUniform, Set<String> visitedRefs) {
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

    private void processBitmap(ProviderDefinition provider, BitmapFont font, FontWidthsFile widthsFile) {
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
            int cellW = texture.getWidth() / cols;
            int cellH = texture.getHeight() / rows;
            int pAscent = provider.getAscent() != null ? provider.getAscent() : 7;
            for (int row = 0; row < rows; row++) {
                String line = chars.get(row);
                for (int col = 0, i = 0; col < cols && i < line.length(); col++) {
                    int cp = line.codePointAt(i);
                    if (cp != 0) {
                        int sx = col * cellW;
                        int sy = row * cellH;
                        int advance = getAdvance(widthsFile, texture, sx, sy, cellW, cellH, cp);
                        double boldOffset = 1.0;
                        double shadowOffset = 1.0;
                        if (widthsFile != null) {
                            FontWidthsFile.CharWidthEntry entry = widthsFile.getCharWidthEntry(cp);
                            if (entry != null) {
                                boldOffset = entry.getBoldOffset();
                                shadowOffset = entry.getShadowOffset();
                            }
                        }
                        Glyph glyph = new Glyph(texture, sx, sy, cellW, cellH, advance, boldOffset, shadowOffset, pAscent);
                        font.putGlyph(cp, glyph);
                    }
                    i += Character.charCount(cp);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to process bitmap provider: {}", e.getMessage());
        }
    }

    private void processSpace(ProviderDefinition provider, BitmapFont font, FontWidthsFile widthsFile) {
        Map<String, Integer> advances = provider.getAdvances();
        if (advances == null) return;
        for (Map.Entry<String, Integer> e : advances.entrySet()) {
            String key = e.getKey();
            if (key == null || key.isEmpty()) continue;
            int cp = key.codePointAt(0);
            font.putAdvance(cp, e.getValue());
            if (widthsFile != null) {
                FontWidthsFile.CharWidthEntry entry = widthsFile.getCharWidthEntry(cp);
                if (entry != null) {
                    font.putBoldOffset(cp, entry.getBoldOffset());
                }
            }
        }
    }

    private static FontWidthsFile loadWidths(String path) {
        try (InputStream in = RESTfulMC.class.getResourceAsStream(path)) {
            if (in != null) {
                return MAPPER.readValue(in, FontWidthsFile.class);
            }
        } catch (Exception e) {
            // optional
        }
        return null;
    }

    private static int getAdvance(FontWidthsFile widths, BufferedImage texture,
                                  int srcX, int srcY, int cellW, int cellH, int codepoint) {
        if (widths != null) {
            int w = widths.getAdvance(codepoint);
            if (w >= 0) return w;
        }
        return Glyph.measureAdvance(texture, srcX, srcY, cellW, cellH);
    }

    public BitmapFont getDefaultFont() {
        load();
        return Objects.requireNonNull(fonts.get(DEFAULT_META), "Font failed to load");
    }
}