package cc.restfulmc.api.common.renderer;

import cc.restfulmc.api.model.skin.Skin;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

@Slf4j
public abstract class SkinRenderer {
    /**
     * Renders the skin part for the player's skin.
     *
     * @param skin the player's skin
     * @param renderOverlays should the overlays be rendered
     * @param size the output size (height; width derived per part)
     * @return the rendered skin part
     */
    public abstract BufferedImage render(Skin skin, boolean renderOverlays, int size);

}
