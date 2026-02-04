package cc.restfulmc.api.common.renderer.impl.skin;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;

@AllArgsConstructor @Getter
public class FaceRenderer extends SkinRenderer {
    public static final FaceRenderer INSTANCE = new FaceRenderer();

    @Override
    public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
        return HeadRenderer.INSTANCE.render(skin, renderOverlays, size, 0, 0);
    }
}
