package cc.restfulmc.api.common.renderer.raster;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.math.Vector3Utils;
import cc.restfulmc.api.common.renderer.IsometricLighting;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 3D isometric renderer: given textures and faces, rotates by yaw/pitch,
 * orthographically projects, depth-sorts, and draws quads.
 * Supports multiple textures (e.g. skin 64×64). Used by full-body and head renderers.
 */
@Slf4j
public class Isometric3DRenderer {
    public static final Isometric3DRenderer INSTANCE = new Isometric3DRenderer();

    /**
     * Renders the given textured face batches with the given view onto an image.
     *
     * @param batches list of (texture, faces) — e.g. skin 64×64
     * @param view    view parameters (eye, target, yaw, pitch, aspect ratio)
     * @param size    output height in pixels; width = size * aspectRatio
     * @return the rendered image
     */
    public BufferedImage render(List<TexturedFaces> batches, ViewParams view, int size) {
        int width = (int) Math.round(size * view.aspectRatio());

        Vector3 eye = view.eye();
        Vector3 target = view.target();
        Vector3 fwd = Vector3Utils.normalize(target.subtract(eye));
        Vector3 right = Vector3Utils.normalize(Vector3Utils.cross(fwd, new Vector3(0, 1, 0)));
        Vector3 up = Vector3Utils.normalize(Vector3Utils.cross(right, fwd));

        double yaw = view.yawDeg();
        double pitch = view.pitchDeg();

        int totalFaces = batches.stream().mapToInt(b -> b.faces().size()).sum();
        List<ProjectedFaceWithTexture> projected = new ArrayList<>(totalFaces);

        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        int faceIndex = 0;
        for (int batchIndex = 0; batchIndex < batches.size(); batchIndex++) {
            TexturedFaces batch = batches.get(batchIndex);
            BufferedImage texture = batch.texture();
            int texW = texture.getWidth();
            int texH = texture.getHeight();

            for (Face face : batch.faces()) {
                var rotatedNormal = Vector3Utils.rotateX(Vector3Utils.rotateY(face.normal(), yaw), pitch);
                double brightness = IsometricLighting.computeBrightness(rotatedNormal, IsometricLighting.SUN_DIRECTION, IsometricLighting.MIN_BRIGHTNESS);

                Vector3 v0 = Vector3Utils.rotAround(face.v0(), target, yaw, pitch);
                Vector3 v1 = Vector3Utils.rotAround(face.v1(), target, yaw, pitch);
                Vector3 v2 = Vector3Utils.rotAround(face.v2(), target, yaw, pitch);
                Vector3 v3 = Vector3Utils.rotAround(face.v3(), target, yaw, pitch);
                double[] p0 = Vector3Utils.project(v0, eye, fwd, right, up);
                double[] p1 = Vector3Utils.project(v1, eye, fwd, right, up);
                double[] p2 = Vector3Utils.project(v2, eye, fwd, right, up);
                double[] p3 = Vector3Utils.project(v3, eye, fwd, right, up);

                double depth = (p0[2] + p1[2] + p2[2] + p3[2]) / 4.0;

                double x0 = p0[0], y0 = p0[1], x1 = p1[0], y1 = p1[1], x2 = p2[0], y2 = p2[1], x3 = p3[0], y3 = p3[1];
                projected.add(new ProjectedFaceWithTexture(
                        x0, y0, x1, y1, x2, y2, x3, y3,
                        depth,
                        face.u0(), face.v0_(), face.u1(), face.v1_(),
                        brightness,
                        batchIndex,
                        texW, texH,
                        faceIndex++
                ));
                minX = Math.min(minX, Math.min(Math.min(x0, x1), Math.min(x2, x3)));
                maxX = Math.max(maxX, Math.max(Math.max(x0, x1), Math.max(x2, x3)));
                minY = Math.min(minY, Math.min(Math.min(y0, y1), Math.min(y2, y3)));
                maxY = Math.max(maxY, Math.max(Math.max(y0, y1), Math.max(y2, y3)));
            }
        }

        // Stable sort: depth back-to-front, then by face index so coplanar faces (e.g. head/body seam) draw consistently
        projected.sort(Comparator.comparingDouble((ProjectedFaceWithTexture p) -> p.depth).reversed()
                .thenComparingInt(p -> p.faceIndex));
        double modelW = maxX - minX;
        double modelH = maxY - minY;
        if (modelW < 1) modelW = 1;
        if (modelH < 1) modelH = 1;
        double scale = Math.min(width / modelW, size / modelH);
        double offsetX = (width - modelW * scale) / 2 - minX * scale;
        double offsetY = maxY * scale;

        BufferedImage result = new BufferedImage(width, size, BufferedImage.TYPE_INT_ARGB);
        int[] outPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();

        // Preload texture pixels per batch (avoids repeated getRGB in loop)
        int[][] batchTexPixels = new int[batches.size()][];
        for (int i = 0; i < batches.size(); i++) {
            batchTexPixels[i] = QuadRasterizer.getTexturePixels(batches.get(i).texture());
        }

        for (ProjectedFaceWithTexture p : projected) {
            int[] texPixels = batchTexPixels[p.textureIndex];
            int texW = p.texW;
            int texH = p.texH;

            double dx0 = p.x0 * scale + offsetX;
            double dy0 = offsetY - p.y0 * scale;
            double dx1 = p.x1 * scale + offsetX;
            double dy1 = offsetY - p.y1 * scale;
            double dx2 = p.x2 * scale + offsetX;
            double dy2 = offsetY - p.y2 * scale;

            // Same texture subrect as old Graphics2D path: floor(u0), floor(v0), ceil(u1)-floor(u0), ceil(v1)-floor(v0)
            int sx1 = (int) Math.floor(p.u0);
            int sy1 = (int) Math.floor(p.v0_);
            int sx2 = (int) Math.ceil(p.u1);
            int sy2 = (int) Math.ceil(p.v1_);
            sx1 = Math.max(0, Math.min(sx1, texW - 1));
            sy1 = Math.max(0, Math.min(sy1, texH - 1));
            sx2 = Math.max(sx1 + 1, Math.min(sx2, texW));
            sy2 = Math.max(sy1 + 1, Math.min(sy2, texH));
            int tw = sx2 - sx1;
            int th = sy2 - sy1;
            if (tw <= 0 || th <= 0) continue;

            QuadRasterizer.rasterizeQuad(
                    outPixels, width, size,
                    dx0, dy0, dx1, dy1, dx2, dy2,
                    sx1, sy1, tw, th,
                    texPixels, texW, texH,
                    (float) p.brightness());
        }
        return result;
    }

    /**
     * Renders faces with a single texture. Convenience for skin-only rendering.
     *
     * @param texture the texture (e.g. 64×64 skin)
     * @param faces   the list of textured faces
     * @param view    view parameters
     * @param size    output height in pixels
     * @return the rendered image
     */
    public BufferedImage render(BufferedImage texture, List<Face> faces, ViewParams view, int size) {
        return render(List.of(new TexturedFaces(texture, faces)), view, size);
    }

    /**
     * View parameters for the 3D isometric renderer.
     * The target is also used as the model rotation center.
     */
    public record ViewParams(Vector3 eye, Vector3 target, double yawDeg,
                             double pitchDeg, double aspectRatio) {}

    /** Pairs a texture with its faces. Used for multi-texture rendering. */
    public record TexturedFaces(BufferedImage texture, List<Face> faces) {}

    private record ProjectedFaceWithTexture(double x0, double y0, double x1, double y1,
                                            double x2, double y2, double x3, double y3,
                                            double depth,
                                            double u0, double v0_, double u1, double v1_,
                                            double brightness,
                                            int textureIndex,
                                            int texW, int texH,
                                            int faceIndex) {}
}
