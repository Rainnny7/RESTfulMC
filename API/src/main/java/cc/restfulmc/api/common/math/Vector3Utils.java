package cc.restfulmc.api.common.math;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Static utilities for 3D vector operations
 * (rotation, projection, normalize, cross).
 *
 * @author Braydon
 */
@UtilityClass
public final class Vector3Utils {
    /**
     * Rotates a point around the Y axis (yaw).
     *
     * @param vector the point to rotate
     * @param degrees angle in degrees
     * @return the rotated point
     */
    @NonNull
    public static Vector3 rotateY(@NonNull Vector3 vector, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Vector3(vector.x() * cos - vector.z() * sin, vector.y(), vector.x() * sin + vector.z() * cos);
    }

    /**
     * Rotates a point around the X axis (pitch).
     *
     * @param vector the point to rotate
     * @param degrees angle in degrees
     * @return the rotated point
     */
    @NonNull
    public static Vector3 rotateX(@NonNull Vector3 vector, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Vector3(vector.x(), vector.y() * cos - vector.z() * sin, vector.y() * sin + vector.z() * cos);
    }

    /**
     * Rotates a vertex around a center point by yaw (Y axis) then pitch (X axis).
     *
     * @param vector the vertex to rotate
     * @param center the rotation center
     * @param yawDegrees yaw angle in degrees
     * @param pitchDegrees pitch angle in degrees
     * @return the rotated vertex
     */
    @NonNull
    public static Vector3 rotAround(@NonNull Vector3 vector, @NonNull Vector3 center, double yawDegrees, double pitchDegrees) {
        Vector3 translated = vector.subtract(center);
        Vector3 rotated = rotateX(rotateY(translated, yawDegrees), pitchDegrees);
        return rotated.add(center);
    }

    /**
     * Orthographically projects a world-space point into view space.
     *
     * @param world the world-space point
     * @param eye the camera position
     * @param forward the camera forward direction
     * @param right the camera right direction
     * @param up the camera up direction
     * @return [viewX, viewY, viewZ] where viewX is right, viewY is up, viewZ is depth
     */
    @NonNull
    public static double[] project(@NonNull Vector3 world, @NonNull Vector3 eye, @NonNull Vector3 forward,
                                   @NonNull Vector3 right, @NonNull Vector3 up) {
        double dx = world.x() - eye.x();
        double dy = world.y() - eye.y();
        double dz = world.z() - eye.z();
        double viewX = dx * right.x() + dy * right.y() + dz * right.z();
        double viewY = dx * up.x() + dy * up.y() + dz * up.z();
        double viewZ = -(dx * forward.x() + dy * forward.y() + dz * forward.z());
        return new double[]{viewX, viewY, viewZ};
    }

    /**
     * Normalizes a vector to unit length.
     *
     * @param vector the vector to normalize
     * @return the normalized vector
     */
    @NonNull
    public static Vector3 normalize(@NonNull Vector3 vector) {
        double length = Math.sqrt(vector.x() * vector.x() + vector.y() * vector.y() + vector.z() * vector.z());
        if (length < 1e-10) {
            return vector;
        }
        return new Vector3(vector.x() / length, vector.y() / length, vector.z() / length);
    }

    /**
     * Computes the dot product of two vectors.
     *
     * @param a the first vector
     * @param b the second vector
     * @return the dot product
     */
    public static double dot(@NonNull Vector3 a, @NonNull Vector3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    /**
     * Computes the cross product of two vectors.
     *
     * @param a the first vector
     * @param b the second vector
     * @return the cross product
     */
    @NonNull
    public static Vector3 cross(@NonNull Vector3 a, @NonNull Vector3 b) {
        return new Vector3(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x()
        );
    }
}
