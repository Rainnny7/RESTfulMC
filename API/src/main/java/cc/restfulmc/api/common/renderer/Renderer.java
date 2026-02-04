package cc.restfulmc.api.common.renderer;

import java.awt.image.BufferedImage;

public abstract class Renderer<T> {
    /**
     * Renders the object to the specified size.
     *
     * @param input The object to render.
     * @param size The size to render the object to.
     */
    public abstract BufferedImage render(T input, int size);
}
