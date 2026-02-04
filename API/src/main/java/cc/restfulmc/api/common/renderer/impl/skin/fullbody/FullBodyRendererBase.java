package cc.restfulmc.api.common.renderer.impl.skin.fullbody;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.renderer.model.impl.PlayerModel;
import cc.restfulmc.api.common.renderer.raster.Face;
import cc.restfulmc.api.common.renderer.raster.Isometric3DRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Renders a full Minecraft player body using the generic 3D isometric pipeline.
 * <p>
 * Coordinates loading/normalizing the skin, building full-body faces, and delegating
 * to {@link Isometric3DRenderer} with view params for FRONT or BACK.
 * </p>
 *
 * @author Braydon
 */
@Slf4j
public final class FullBodyRendererBase {
    /**
     * The singleton instance.
     */
    public static final FullBodyRendererBase INSTANCE = new FullBodyRendererBase();

    /**
     * The aspect ratio for the output image.
     */
    private static final double ASPECT_RATIO = 512.0 / 869.0;

    /**
     * The default pitch angle in degrees.
     */
    private static final double PITCH_DEG = 45.0;

    /**
     * The default yaw angle in degrees.
     */
    private static final double YAW_DEG = 45.0;

    /**
     * The camera eye position.
     */
    private static final Vector3 EYE = new Vector3(0, 28, -45);

    /**
     * The camera target position.
     */
    private static final Vector3 TARGET = new Vector3(0, 16.5, 0);

    /**
     * Renders the full body with default view angles.
     *
     * @param skin the skin
     * @param side the side to render (front or back)
     * @param renderOverlays whether to include overlay layer
     * @param size output height in pixels
     * @return the rendered image
     */
    @SneakyThrows @NonNull
    public BufferedImage render(@NonNull Skin skin, @NonNull Side side, boolean renderOverlays, int size) {
        return render(skin, side, renderOverlays, size, YAW_DEG, PITCH_DEG);
    }

    /**
     * Renders the full body with custom view angles.
     *
     * @param skin the skin
     * @param side the side to render (front or back)
     * @param renderOverlays whether to include overlay layer
     * @param size output height in pixels
     * @param yawDeg view yaw in degrees
     * @param pitchDeg view pitch in degrees
     * @return the rendered image
     */
    @SneakyThrows @NonNull
    public BufferedImage render(@NonNull Skin skin, @NonNull Side side, boolean renderOverlays, int size, double yawDeg, double pitchDeg) {
        List<Face> faces = PlayerModel.buildFaces(skin, renderOverlays);
        double yaw = yawDeg + (side == Side.BACK ? 180.0 : 0.0);
        Isometric3DRenderer.ViewParams view = new Isometric3DRenderer.ViewParams(EYE, TARGET, yaw, pitchDeg, ASPECT_RATIO);
        return Isometric3DRenderer.INSTANCE.render(skin.getImage(), faces, view, size);
    }

    /**
     * The side of the body to render.
     *
     * @author Braydon
     */
    public enum Side {
        /**
         * Front side of the body.
         */
        FRONT,

        /**
         * Back side of the body.
         */
        BACK
    }
}
