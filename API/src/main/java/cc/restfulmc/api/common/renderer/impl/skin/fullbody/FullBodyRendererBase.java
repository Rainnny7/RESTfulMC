package cc.restfulmc.api.common.renderer.impl.skin.fullbody;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.renderer.model.impl.PlayerModel;
import cc.restfulmc.api.common.renderer.raster.Face;
import cc.restfulmc.api.common.renderer.raster.Isometric3DRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Renders a full Minecraft player body using the generic 3D isometric pipeline.
 * Coordinates loading/normalizing the skin, building full-body faces, and delegating
 * to {@link Isometric3DRenderer} with view params for FRONT or BACK.
 */
@Slf4j
public class FullBodyRendererBase {
    public static final FullBodyRendererBase INSTANCE = new FullBodyRendererBase();

    private static final double ASPECT_RATIO = 512.0 / 869.0;
    private static final double PITCH_DEG = 45.0;
    private static final double YAW_DEG = 45.0;
    private static final Vector3 EYE = new Vector3(0, 28, -45);
    private static final Vector3 TARGET = new Vector3(0, 16.5, 0);

    @SneakyThrows
    public BufferedImage render(Skin skin, Side side, boolean renderOverlays, int size) {
        return render(skin, side, renderOverlays, size, YAW_DEG, PITCH_DEG);
    }

    /**
     * Renders the full body with custom view angles.
     */
    @SneakyThrows
    public BufferedImage render(Skin skin, Side side, boolean renderOverlays, int size, double yawDeg, double pitchDeg) {
        List<Face> faces = PlayerModel.buildFaces(skin, renderOverlays);
        double yaw = yawDeg + (side == Side.BACK ? 180.0 : 0.0);
        Isometric3DRenderer.ViewParams view = new Isometric3DRenderer.ViewParams(EYE, TARGET, yaw, pitchDeg, ASPECT_RATIO);
        return Isometric3DRenderer.INSTANCE.render(skin.getImage(), faces, view, size);
    }

    /**
     * The side of the body to render.
     */
    public enum Side {
        FRONT,
        BACK
    }
}
