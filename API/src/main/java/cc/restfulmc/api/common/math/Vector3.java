package cc.restfulmc.api.common.math;

/**
 * 3D vector for software 3D rendering.
 */
public record Vector3(double x, double y, double z) {
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public Vector3 transform(Matrix4 m) {
        double w = m.m03 * x + m.m13 * y + m.m23 * z + m.m33;
        if (Math.abs(w) < 1e-10) {
            w = 1.0;
        }
        return new Vector3(
                (m.m00 * x + m.m10 * y + m.m20 * z + m.m30) / w,
                (m.m01 * x + m.m11 * y + m.m21 * z + m.m31) / w,
                (m.m02 * x + m.m12 * y + m.m22 * z + m.m32) / w
        );
    }
}
