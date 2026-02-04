package cc.restfulmc.api.common.renderer.impl.skin.fullbody;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.model.player.skin.Skin;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;

/**
 * Renders the full player body from the back.
 *
 * @author Braydon
 */
public final class FullBodyRendererBack extends SkinRenderer {
    /**
     * The singleton instance.
     */
    public static final FullBodyRendererBack INSTANCE = new FullBodyRendererBack();

    /**
     * Renders the skin part for the player's skin.
     *
     * @param skin the player's skin
     * @param renderOverlays should the overlays be rendered
     * @param size the output size (height; width derived per part)
     * @return the rendered skin part
     */
    @Override @SneakyThrows @NonNull
    public BufferedImage render(@NonNull Skin skin, boolean renderOverlays, int size) {
        return FullBodyRendererBase.INSTANCE.render(skin, FullBodyRendererBase.Side.BACK, renderOverlays, size);
    }
}
