package cc.restfulmc.api.common.renderer.raster;

import cc.restfulmc.api.common.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * A textured quad with 4 vertices, UV coordinates, and outward normal
 * for software 3D rendering.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public final class Face {
    /**
     * The first vertex (top-left).
     */
    @NonNull private final Vector3 v0;

    /**
     * The second vertex (top-right).
     */
    @NonNull private final Vector3 v1;

    /**
     * The third vertex (bottom-left).
     */
    @NonNull private final Vector3 v2;

    /**
     * The fourth vertex (bottom-right).
     */
    @NonNull private final Vector3 v3;

    /**
     * The U coordinate start.
     */
    private final double u0;

    /**
     * The V coordinate start.
     */
    private final double v0_;

    /**
     * The U coordinate end.
     */
    private final double u1;

    /**
     * The V coordinate end.
     */
    private final double v1_;

    /**
     * The face normal vector.
     */
    @NonNull private final Vector3 normal;
}
