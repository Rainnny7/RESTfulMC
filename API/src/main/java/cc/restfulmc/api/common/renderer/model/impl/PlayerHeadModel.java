package cc.restfulmc.api.common.renderer.model.impl;

import cc.restfulmc.api.common.renderer.model.ModelUtils;
import cc.restfulmc.api.common.renderer.model.PlayerModelCoordinates;
import cc.restfulmc.api.common.renderer.raster.Face;
import cc.restfulmc.api.model.skin.Skin;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minecraft player head model for software 3D rendering.
 * <p>
 * Same coordinate system as PlayerModel: Y up, front at -Z.
 * </p>
 *
 * @author Braydon
 */
public final class PlayerHeadModel {
    /**
     * Cache for pre-built face lists.
     */
    private static final Map<FaceCacheKey, List<Face>> FACE_CACHE = new ConcurrentHashMap<>();

    /**
     * Builds faces for the head only (base layer and optional overlay).
     *
     * @param skin the skin
     * @param renderOverlays whether to include the overlay layer
     * @return the list of textured faces (unmodifiable, may be shared)
     */
    @NonNull
    public static List<Face> buildFaces(@NonNull Skin skin, boolean renderOverlays) {
        boolean slim = skin.getModel() == Skin.Model.SLIM;
        boolean legacy = skin.isLegacy();
        return FACE_CACHE.computeIfAbsent(new FaceCacheKey(slim, renderOverlays), key -> buildFacesUncached(key.isSlim(), key.isRenderOverlays() && !legacy));
    }

    /**
     * Builds faces without caching.
     *
     * @param slim whether to use slim model
     * @param renderOverlays whether to include overlays
     * @return the face list
     */
    @NonNull
    private static List<Face> buildFacesUncached(boolean slim, boolean renderOverlays) {
        List<Face> faces = new ArrayList<>();

        // Base layer: head box at -4, 24, -4, size 8x8x8
        ModelUtils.addBox(faces, -4, 24, -4, 8, 8, 8, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.HEAD.getBaseUv(slim)));

        if (renderOverlays) {
            // Overlay layer: slightly larger head box
            ModelUtils.addBox(faces, -4.5, 23.5, -4.5, 9, 9, 9, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.HEAD.getOverlayUv(slim)));
        }

        return Collections.unmodifiableList(faces);
    }

    /**
     * Cache key for face lists.
     *
     * @author Braydon
     */
    @AllArgsConstructor @Getter @EqualsAndHashCode
    private static final class FaceCacheKey {
        /**
         * Whether slim arms are used.
         */
        private final boolean slim;

        /**
         * Whether overlays are rendered.
         */
        private final boolean renderOverlays;
    }
}
