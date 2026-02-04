package cc.restfulmc.api.common.renderer.impl.skin;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.common.renderer.impl.skin.fullbody.FullBodyRendererBase;
import cc.restfulmc.api.model.player.skin.Skin;
import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * Renders the player body with a custom pitch for a slight top-down view.
 *
 * @author Braydon
 */
public final class BodyRenderer extends SkinRenderer {
    /**
     * The singleton instance.
     */
    public static final BodyRenderer INSTANCE = new BodyRenderer();

    /**
     * Renders the skin part for the player's skin.
     *
     * @param skin the player's skin
     * @param renderOverlays should the overlays be rendered
     * @param size the output size (height; width derived per part)
     * @return the rendered skin part
     */
    @Override @NonNull
    public BufferedImage render(@NonNull Skin skin, boolean renderOverlays, int size) {
        return FullBodyRendererBase.INSTANCE.render(skin, FullBodyRendererBase.Side.FRONT, renderOverlays, size, 0, 14.2);
    }
}
