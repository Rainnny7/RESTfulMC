package cc.restfulmc.api.common.math;

/**
 * Static utilities for 3D vector operations (rotation, projection, normalize, cross).
 */
public final class Vector3Utils {
    /**
     * Rotates a point around the Y axis (yaw).
     *
     * @param v   the point to rotate
     * @param deg angle in degrees
     * @return the rotated point
     */
    public static Vector3 rotateY(Vector3 v, double deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad), s = Math.sin(rad);
        return new Vector3(v.x() * c - v.z() * s, v.y(), v.x() * s + v.z() * c);
    }

    /**
     * Rotates a point around the X axis (pitch).
     *
     * @param v   the point to rotate
     * @param deg angle in degrees
     * @return the rotated point
     */
    public static Vector3 rotateX(Vector3 v, double deg) {
        double rad = Math.toRadians(deg);
        double c = Math.cos(rad), s = Math.sin(rad);
        return new Vector3(v.x(), v.y() * c - v.z() * s, v.y() * s + v.z() * c);
    }

    /**
     * Rotates a vertex around a center point by yaw (Y axis) then pitch (X axis).
     *
     * @param v         the vertex to rotate
     * @param center    the rotation center
     * @param yawDeg    yaw angle in degrees
     * @param pitchDeg  pitch angle in degrees
     * @return the rotated vertex
     */
    public static Vector3 rotAround(Vector3 v, Vector3 center, double yawDeg, double pitchDeg) {
        Vector3 t = v.subtract(center);
        Vector3 r = rotateX(rotateY(t, yawDeg), pitchDeg);
        return r.add(center);
    }

    /**
     * Orthographically projects a world-space point into view space.
     *
     * @param world the world-space point
     * @param eye   the camera position
     * @param fwd   the camera forward direction
     * @param right the camera right direction
     * @param up    the camera up direction
     * @return [viewX, viewY, viewZ] where viewX is right, viewY is up, viewZ is depth
     */
    public static double[] project(Vector3 world, Vector3 eye, Vector3 fwd, Vector3 right, Vector3 up) {
        double dx = world.x() - eye.x();
        double dy = world.y() - eye.y();
        double dz = world.z() - eye.z();
        double viewX = dx * right.x() + dy * right.y() + dz * right.z();
        double viewY = dx * up.x() + dy * up.y() + dz * up.z();
        double viewZ = -(dx * fwd.x() + dy * fwd.y() + dz * fwd.z());
        return new double[]{viewX, viewY, viewZ};
    }

    /** Normalizes a vector to unit length. */
    public static Vector3 normalize(Vector3 v) {
        double len = Math.sqrt(v.x() * v.x() + v.y() * v.y() + v.z() * v.z());
        if (len < 1e-10) return v;
        return new Vector3(v.x() / len, v.y() / len, v.z() / len);
    }

    /** Dot product of two vectors. */
    public static double dot(Vector3 a, Vector3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    /** Cross product of two vectors. */
    public static Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x()
        );
    }
}
