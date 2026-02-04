package cc.restfulmc.api.common.math;

/**
 * A 4x4 matrix for view/projection in software 3D rendering.
 * <p>
 * Row-major layout: m[row][col] -> mXY where X=row, Y=col.
 * </p>
 *
 * @author Braydon
 */
public final class Matrix4 {
    /**
     * Matrix element at row 0, column 0.
     */
    public double m00;

    /**
     * Matrix element at row 0, column 1.
     */
    public double m01;

    /**
     * Matrix element at row 0, column 2.
     */
    public double m02;

    /**
     * Matrix element at row 0, column 3.
     */
    public double m03;

    /**
     * Matrix element at row 1, column 0.
     */
    public double m10;

    /**
     * Matrix element at row 1, column 1.
     */
    public double m11;

    /**
     * Matrix element at row 1, column 2.
     */
    public double m12;

    /**
     * Matrix element at row 1, column 3.
     */
    public double m13;

    /**
     * Matrix element at row 2, column 0.
     */
    public double m20;

    /**
     * Matrix element at row 2, column 1.
     */
    public double m21;

    /**
     * Matrix element at row 2, column 2.
     */
    public double m22;

    /**
     * Matrix element at row 2, column 3.
     */
    public double m23;

    /**
     * Matrix element at row 3, column 0.
     */
    public double m30;

    /**
     * Matrix element at row 3, column 1.
     */
    public double m31;

    /**
     * Matrix element at row 3, column 2.
     */
    public double m32;

    /**
     * Matrix element at row 3, column 3.
     */
    public double m33;

    public Matrix4() {
        setIdentity();
    }

    /**
     * Sets this matrix to the identity matrix.
     */
    public void setIdentity() {
        m00 = 1; m01 = 0; m02 = 0; m03 = 0;
        m10 = 0; m11 = 1; m12 = 0; m13 = 0;
        m20 = 0; m21 = 0; m22 = 1; m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 1;
    }
}
