package cc.restfulmc.api.common.renderer.impl.skin;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.model.skin.Skin;
import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * Renders the player face (front view of the head).
 *
 * @author Braydon
 */
public final class FaceRenderer extends SkinRenderer {
    /**
     * The singleton instance.
     */
    public static final FaceRenderer INSTANCE = new FaceRenderer();

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
        return HeadRenderer.INSTANCE.render(skin, renderOverlays, size, 0, 0);
    }
}
