package cc.restfulmc.api.common.renderer.impl.skin;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.common.renderer.model.impl.PlayerHeadModel;
import cc.restfulmc.api.common.renderer.raster.Face;
import cc.restfulmc.api.common.renderer.raster.Isometric3DRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.util.List;

public class HeadRenderer extends SkinRenderer {
    public static final HeadRenderer INSTANCE = new HeadRenderer();

    private static final double PITCH_DEG = 35.0;
    private static final double YAW_DEG = 45.0;
    private static final double ASPECT_RATIO = 1.0;
    /** Head center in model space; eye is along -Z so head fills frame. */
    private static final Vector3 HEAD_TARGET = new Vector3(0, 28, 0);
    private static final Vector3 HEAD_EYE = new Vector3(0, 28, -20);

    @Override @SneakyThrows
    public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
        return render(skin, renderOverlays, size, YAW_DEG, PITCH_DEG);
    }

    /**
     * Renders the head with custom view angles for better overlay visibility.
     *
     * @param skin           the skin
     * @param renderOverlays whether to include overlay layer
     * @param size           output height in pixels
     * @param yawDeg         view yaw in degrees
     * @param pitchDeg       view pitch in degrees
     * @return the rendered image
     */
    @SneakyThrows
    public BufferedImage render(Skin skin, boolean renderOverlays, int size, double yawDeg, double pitchDeg) {
        List<Face> faces = PlayerHeadModel.buildFaces(skin, renderOverlays);
        Isometric3DRenderer.ViewParams view = new Isometric3DRenderer.ViewParams(HEAD_EYE, HEAD_TARGET, yawDeg, pitchDeg, ASPECT_RATIO);
        return Isometric3DRenderer.INSTANCE.render(skin.getImage(), faces, view, size);
    }
}
