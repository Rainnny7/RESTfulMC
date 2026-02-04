package cc.restfulmc.api.common.math;

/**
 * 4x4 matrix for view/projection in software 3D rendering.
 * Row-major layout: m[row][col] -> mXY where X=row, Y=col
 */
public class Matrix4 {
    public double m00, m01, m02, m03;
    public double m10, m11, m12, m13;
    public double m20, m21, m22, m23;
    public double m30, m31, m32, m33;

    public Matrix4() {
        setIdentity();
    }

    public void setIdentity() {
        m00 = 1; m01 = 0; m02 = 0; m03 = 0;
        m10 = 0; m11 = 1; m12 = 0; m13 = 0;
        m20 = 0; m21 = 0; m22 = 1; m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 1;
    }
}
