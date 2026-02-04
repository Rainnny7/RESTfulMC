package cc.restfulmc.api.common.renderer.model;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.renderer.raster.Face;

import java.util.List;

/**
 * Static helpers for building box meshes (UV and face generation) used by
 * PlayerModel and PlayerHeadModel.
 */
public final class ModelUtils {

    /**
     * Converts ModelBox UV array (x, y, sizeX, sizeY, sizeZ) to boxUv format.
     */
    public static double[][] uvFrom(int[] uv) {
        return boxUv(uv[0], uv[1], uv[2], uv[3], uv[4]);
    }

    /**
     * Computes UV rects for a box per face (north, south, east, west, up, down).
     * Order: north (-Z front), south (+Z back), east (+X), west (-X), up (+Y), down (-Y).
     */
    public static double[][] boxUv(int x, int y, int sizeX, int sizeY, int sizeZ) {
        return new double[][]{
                {x, y, x + sizeX, y + sizeY},                           // north (-Z front)
                {x + sizeX + sizeZ, y, x + 2 * sizeX + sizeZ, y + sizeY}, // south (+Z back)
                {x - sizeZ, y, x, y + sizeY},                           // east (+X)
                {x + sizeX, y, x + sizeX + sizeZ, y + sizeY},           // west (-X)
                {x, y - sizeZ, x + sizeX, y},                           // up (+Y)
                {x + sizeX, y - sizeZ, x + 2 * sizeX, y}                // down (-Y)
        };
    }

    /**
     * Adds a box (6 faces) to the face list. Front at -Z, back at +Z.
     */
    public static void addBox(List<Face> faces, double px, double py, double pz,
                              double w, double h, double d, double[][] uvs) {
        double x1 = px + w;
        double y1 = py + h;
        double z1 = pz + d;

        // north (front at -Z), south (back at +Z), up, down, west (-X), east (+X)
        faces.add(new Face(
                new Vector3(x1, y1, pz), new Vector3(px, y1, pz),
                new Vector3(x1, py, pz), new Vector3(px, py, pz),
                uvs[1][0], uvs[1][1], uvs[1][2], uvs[1][3],
                new Vector3(0, 0, -1)));

        faces.add(new Face(
                new Vector3(x1, y1, z1), new Vector3(px, y1, z1),
                new Vector3(x1, py, z1), new Vector3(px, py, z1),
                uvs[0][0], uvs[0][1], uvs[0][2], uvs[0][3],
                new Vector3(0, 0, 1)));

        faces.add(new Face(
                new Vector3(x1, y1, pz), new Vector3(px, y1, pz),
                new Vector3(x1, y1, z1), new Vector3(px, y1, z1),
                uvs[4][0], uvs[4][1], uvs[4][2], uvs[4][3],
                new Vector3(0, 1, 0)));

        faces.add(new Face(
                new Vector3(px, py, pz), new Vector3(x1, py, pz),
                new Vector3(px, py, z1), new Vector3(x1, py, z1),
                uvs[5][2], uvs[5][1], uvs[5][0], uvs[5][3],
                new Vector3(0, -1, 0)));

        faces.add(new Face(
                new Vector3(px, y1, z1), new Vector3(px, y1, pz),
                new Vector3(px, py, z1), new Vector3(px, py, pz),
                uvs[3][0], uvs[3][1], uvs[3][2], uvs[3][3],
                new Vector3(-1, 0, 0)));

        faces.add(new Face(
                new Vector3(x1, y1, pz), new Vector3(x1, y1, z1),
                new Vector3(x1, py, pz), new Vector3(x1, py, z1),
                uvs[2][0], uvs[2][1], uvs[2][2], uvs[2][3],
                new Vector3(1, 0, 0)));
    }
}
