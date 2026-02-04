package cc.restfulmc.api.common.math;

import lombok.NonNull;

/**
 * A 3D vector for software 3D rendering.
 *
 * @author Braydon
 */
public record Vector3(double x, double y, double z) {
    /**
     * Adds another vector to this vector.
     *
     * @param other the vector to add
     * @return the resulting vector
     */
    @NonNull
    public Vector3 add(@NonNull Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other the vector to subtract
     * @return the resulting vector
     */
    @NonNull
    public Vector3 subtract(@NonNull Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Multiplies this vector by a scalar.
     *
     * @param scalar the scalar to multiply by
     * @return the resulting vector
     */
    @NonNull
    public Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Transforms this vector by a 4x4 matrix.
     *
     * @param matrix the matrix to transform by
     * @return the transformed vector
     */
    @NonNull
    public Vector3 transform(@NonNull Matrix4 matrix) {
        double w = matrix.m03 * x + matrix.m13 * y + matrix.m23 * z + matrix.m33;
        if (Math.abs(w) < 1e-10) {
            w = 1.0;
        }
        return new Vector3(
                (matrix.m00 * x + matrix.m10 * y + matrix.m20 * z + matrix.m30) / w,
                (matrix.m01 * x + matrix.m11 * y + matrix.m21 * z + matrix.m31) / w,
                (matrix.m02 * x + matrix.m12 * y + matrix.m22 * z + matrix.m32) / w
        );
    }
}
