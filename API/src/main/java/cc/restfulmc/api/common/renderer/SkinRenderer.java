package cc.restfulmc.api.common.renderer;

import cc.restfulmc.api.model.skin.Skin;
import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * Base class for skin part renderers.
 *
 * @author Braydon
 */
public abstract class SkinRenderer {
    /**
     * Renders the skin part for the player's skin.
     *
     * @param skin the player's skin
     * @param renderOverlays should the overlays be rendered
     * @param size the output size (height; width derived per part)
     * @return the rendered skin part
     */
    @NonNull
    public abstract BufferedImage render(@NonNull Skin skin, boolean renderOverlays, int size);
}
