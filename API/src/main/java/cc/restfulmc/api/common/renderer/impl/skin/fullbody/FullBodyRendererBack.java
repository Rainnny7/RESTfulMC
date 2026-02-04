package cc.restfulmc.api.common.renderer.impl.skin.fullbody;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;

public class FullBodyRendererBack extends SkinRenderer {
    public static final FullBodyRendererBack INSTANCE = new FullBodyRendererBack();

    @Override
    @SneakyThrows
    public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
        return FullBodyRendererBase.INSTANCE.render(skin, FullBodyRendererBase.Side.BACK, renderOverlays, size);
    }
}
