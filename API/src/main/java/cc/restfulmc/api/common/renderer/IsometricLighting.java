package cc.restfulmc.api.common.renderer;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.math.Vector3Utils;

/**
 * Canonical sun-based directional lighting for isometric 3D rendering.
 */
public final class IsometricLighting {
    public static final double MIN_BRIGHTNESS = 0.65;
    public static final Vector3 SUN_DIRECTION = Vector3Utils.normalize(new Vector3(-1, 1, 0.5));

    /**
     * Computes brightness for a face based on its normal and sun direction.
     *
     * @param normal       world-space face normal (will be normalized)
     * @param sunDirection sun direction in world space (should be normalized)
     * @param minBrightness minimum brightness floor [0, 1]
     * @return brightness in [0, 1]
     */
    public static double computeBrightness(Vector3 normal, Vector3 sunDirection, double minBrightness) {
        Vector3 n = Vector3Utils.normalize(normal);
        double dot = Vector3Utils.dot(n, sunDirection);
        return Math.max(0, Math.min(1, minBrightness + (1.0 - minBrightness) * (1.0 + dot) * 0.5));
    }
}
