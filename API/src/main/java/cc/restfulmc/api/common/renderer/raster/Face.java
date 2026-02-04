package cc.restfulmc.api.common.renderer.raster;

import cc.restfulmc.api.common.math.Vector3;

/**
 * A textured quad with 4 vertices, UV coordinates, and outward normal for software 3D rendering.
 */
public record Face(Vector3 v0, Vector3 v1, Vector3 v2, Vector3 v3, double u0, double v0_, double u1, double v1_,
                   Vector3 normal) {
}
