package cc.restfulmc.api.common.renderer;

import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * Base class for renderers that convert objects to images.
 *
 * @param <T> the type of object to render
 * @author Braydon
 */
public abstract class Renderer<T> {
    /**
     * Renders the object to the specified size.
     *
     * @param input the object to render
     * @param size the size to render the object to
     * @return the rendered image
     */
    @NonNull
    public abstract BufferedImage render(@NonNull T input, int size);
}
