package cc.restfulmc.api.common.renderer.impl.skin.fullbody;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;

public class FullBodyRendererFront extends SkinRenderer {
    public static final FullBodyRendererFront INSTANCE = new FullBodyRendererFront();

    @Override
    @SneakyThrows
    public BufferedImage render(Skin skin, boolean renderOverlays, int size) {
        return FullBodyRendererBase.INSTANCE.render(skin, FullBodyRendererBase.Side.FRONT, renderOverlays, size);
    }
}
