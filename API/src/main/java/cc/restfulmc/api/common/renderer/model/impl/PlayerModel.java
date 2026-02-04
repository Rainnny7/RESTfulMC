package cc.restfulmc.api.common.renderer.model.impl;

import cc.restfulmc.api.common.renderer.model.ModelUtils;
import cc.restfulmc.api.common.renderer.model.PlayerModelCoordinates;
import cc.restfulmc.api.common.renderer.raster.Face;
import cc.restfulmc.api.model.skin.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minecraft player model for software 3D rendering.
 * Coordinates: Y up, front at -Z, pos is min corner.
 */
public class PlayerModel {
    private static final Map<FaceCacheKey, List<Face>> FACE_CACHE = new ConcurrentHashMap<>();

    /**
     * Builds all faces for the player model.
     *
     * @param skin           the skin (determines slim/classic arms)
     * @param renderOverlays whether to include the overlay layer
     * @return the list of textured faces (unmodifiable, may be shared)
     */
    public static List<Face> buildFaces(Skin skin, boolean renderOverlays) {
        boolean slim = skin.getModel() == Skin.Model.SLIM;
        boolean legacy = skin.isLegacy();
        return FACE_CACHE.computeIfAbsent(new FaceCacheKey(slim, renderOverlays && !legacy), cacheKey -> {
            List<Face> faces = new ArrayList<>();

            // Base layer: head, body, left arm, right arm, left leg, right leg
            ModelUtils.addBox(faces, -4, 24, -4, 8, 8, 8, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.HEAD.getBaseUv(slim)));
            ModelUtils.addBox(faces, -4, 12, -2, 8, 12, 4, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.BODY.getBaseUv(slim)));
            ModelUtils.addBox(faces, slim ? -7 : -8, 12, -2, slim ? 3 : 4, 12, 4, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.LEFT_ARM.getBaseUv(slim)));
            ModelUtils.addBox(faces, 4, 12, -2, slim ? 3 : 4, 12, 4, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.RIGHT_ARM.getBaseUv(slim)));
            ModelUtils.addBox(faces, -4, 0, -2, 4, 12, 4, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.LEFT_LEG.getBaseUv(slim)));
            ModelUtils.addBox(faces, 0, 0, -2, 4, 12, 4, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.RIGHT_LEG.getBaseUv(slim)));

            if (cacheKey.renderOverlays()) {
                // Overlay layer: slightly larger boxes with second-layer UVs
                ModelUtils.addBox(faces, -4.5, 23.5, -4.5, 9, 9, 9, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.HEAD.getOverlayUv(slim)));
                ModelUtils.addBox(faces, -4.25, 11.75, -2.25, 8.5, 12.5, 4.5, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.BODY.getOverlayUv(slim)));
                ModelUtils.addBox(faces, slim ? -7.25 : -8.25, 11.75, -2.25, slim ? 3.5 : 4.5, 12.5, 4.5, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.LEFT_ARM.getOverlayUv(slim)));
                ModelUtils.addBox(faces, 3.75, 11.75, -2.25, slim ? 3.5 : 4.5, 12.5, 4.5, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.RIGHT_ARM.getOverlayUv(slim)));
                ModelUtils.addBox(faces, -4.25, -0.25, -2.25, 4.5, 12.5, 4.5, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.LEFT_LEG.getOverlayUv(slim)));
                ModelUtils.addBox(faces, -0.25, -0.25, -2.25, 4.5, 12.5, 4.5, ModelUtils.uvFrom(PlayerModelCoordinates.ModelBox.RIGHT_LEG.getOverlayUv(slim)));
            }
            return Collections.unmodifiableList(faces);
        });
    }

    private record FaceCacheKey(boolean slim, boolean renderOverlays) { }
}
