package cc.restfulmc.api.common.renderer;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.math.Vector3Utils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Canonical sun-based directional lighting for isometric 3D rendering.
 *
 * @author Braydon
 */
@UtilityClass
public final class IsometricLighting {
    /**
     * The minimum brightness floor.
     */
    public static final double MIN_BRIGHTNESS = 0.65;

    /**
     * The normalized sun direction vector.
     */
    public static final Vector3 SUN_DIRECTION = Vector3Utils.normalize(new Vector3(-1, 1, 0.5));

    /**
     * Computes brightness for a face based on its normal and sun direction.
     *
     * @param normal the world-space face normal (will be normalized)
     * @param sunDirection the sun direction in world space (should be normalized)
     * @param minBrightness the minimum brightness floor [0, 1]
     * @return brightness in [0, 1]
     */
    public static double computeBrightness(@NonNull Vector3 normal, @NonNull Vector3 sunDirection, double minBrightness) {
        Vector3 normalizedNormal = Vector3Utils.normalize(normal);
        double dot = Vector3Utils.dot(normalizedNormal, sunDirection);
        return Math.max(0, Math.min(1, minBrightness + (1.0 - minBrightness) * (1.0 + dot) * 0.5));
    }
}
