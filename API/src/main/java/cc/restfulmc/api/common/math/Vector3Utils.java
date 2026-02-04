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
        return new Vector3(vector.getX() * cos - vector.getZ() * sin, vector.getY(), vector.getX() * sin + vector.getZ() * cos);
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
        return new Vector3(vector.getX(), vector.getY() * cos - vector.getZ() * sin, vector.getY() * sin + vector.getZ() * cos);
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
        double dx = world.getX() - eye.getX();
        double dy = world.getY() - eye.getY();
        double dz = world.getZ() - eye.getZ();
        double viewX = dx * right.getX() + dy * right.getY() + dz * right.getZ();
        double viewY = dx * up.getX() + dy * up.getY() + dz * up.getZ();
        double viewZ = -(dx * forward.getX() + dy * forward.getY() + dz * forward.getZ());
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
        double length = Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
        if (length < 1e-10) {
            return vector;
        }
        return new Vector3(vector.getX() / length, vector.getY() / length, vector.getZ() / length);
    }

    /**
     * Computes the dot product of two vectors.
     *
     * @param a the first vector
     * @param b the second vector
     * @return the dot product
     */
    public static double dot(@NonNull Vector3 a, @NonNull Vector3 b) {
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
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
                a.getY() * b.getZ() - a.getZ() * b.getY(),
                a.getZ() * b.getX() - a.getX() * b.getZ(),
                a.getX() * b.getY() - a.getY() * b.getX()
        );
    }
}
