package cc.restfulmc.api.common.renderer.impl.skin;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.common.renderer.impl.skin.fullbody.FullBodyRendererBase;
import cc.restfulmc.api.model.skin.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;

@AllArgsConstructor @Getter
public class BodyRenderer extends SkinRenderer {
    public static final BodyRenderer INSTANCE = new BodyRenderer();

    @Override
    public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
        return FullBodyRendererBase.INSTANCE.render(skin, FullBodyRendererBase.Side.FRONT, renderOverlays, size, 0, 14.2);
    }
}
