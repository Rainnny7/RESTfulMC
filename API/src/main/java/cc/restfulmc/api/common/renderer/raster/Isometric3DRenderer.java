package cc.restfulmc.api.common.renderer.raster;

import cc.restfulmc.api.common.math.Vector3;
import cc.restfulmc.api.common.math.Vector3Utils;
import cc.restfulmc.api.common.renderer.IsometricLighting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 3D isometric renderer: given textures and faces, rotates by yaw/pitch,
 * orthographically projects, depth-sorts, and draws quads.
 * <p>
 * Supports multiple textures (e.g. skin 64x64). Used by full-body and head renderers.
 * </p>
 *
 * @author Braydon
 */
@Slf4j
public final class Isometric3DRenderer {
    /**
     * The singleton instance.
     */
    public static final Isometric3DRenderer INSTANCE = new Isometric3DRenderer();

    /**
     * Renders the given textured face batches with the given view onto an image.
     *
     * @param batches list of (texture, faces) - e.g. skin 64x64
     * @param view view parameters (eye, target, yaw, pitch, aspect ratio)
     * @param size output height in pixels; width = size * aspectRatio
     * @return the rendered image
     */
    @NonNull
    public BufferedImage render(@NonNull List<TexturedFaces> batches, @NonNull ViewParams view, int size) {
        int width = (int) Math.round(size * view.getAspectRatio());

        Vector3 eye = view.getEye();
        Vector3 target = view.getTarget();
        Vector3 forward = Vector3Utils.normalize(target.subtract(eye));
        Vector3 right = Vector3Utils.normalize(Vector3Utils.cross(forward, new Vector3(0, 1, 0)));
        Vector3 up = Vector3Utils.normalize(Vector3Utils.cross(right, forward));

        double yaw = view.getYawDeg();
        double pitch = view.getPitchDeg();

        int totalFaces = batches.stream().mapToInt(batch -> batch.getFaces().size()).sum();
        List<ProjectedFaceWithTexture> projected = new ArrayList<>(totalFaces);

        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        int faceIndex = 0;
        for (int batchIndex = 0; batchIndex < batches.size(); batchIndex++) {
            TexturedFaces batch = batches.get(batchIndex);
            BufferedImage texture = batch.getTexture();
            int texWidth = texture.getWidth();
            int texHeight = texture.getHeight();

            for (Face face : batch.getFaces()) {
                Vector3 rotatedNormal = Vector3Utils.rotateX(Vector3Utils.rotateY(face.getNormal(), yaw), pitch);
                double brightness = IsometricLighting.computeBrightness(rotatedNormal, IsometricLighting.SUN_DIRECTION, IsometricLighting.MIN_BRIGHTNESS);

                Vector3 v0 = Vector3Utils.rotAround(face.getV0(), target, yaw, pitch);
                Vector3 v1 = Vector3Utils.rotAround(face.getV1(), target, yaw, pitch);
                Vector3 v2 = Vector3Utils.rotAround(face.getV2(), target, yaw, pitch);
                Vector3 v3 = Vector3Utils.rotAround(face.getV3(), target, yaw, pitch);
                double[] p0 = Vector3Utils.project(v0, eye, forward, right, up);
                double[] p1 = Vector3Utils.project(v1, eye, forward, right, up);
                double[] p2 = Vector3Utils.project(v2, eye, forward, right, up);
                double[] p3 = Vector3Utils.project(v3, eye, forward, right, up);

                double depth = (p0[2] + p1[2] + p2[2] + p3[2]) / 4.0;

                double x0 = p0[0], y0 = p0[1], x1 = p1[0], y1 = p1[1], x2 = p2[0], y2 = p2[1], x3 = p3[0], y3 = p3[1];
                projected.add(new ProjectedFaceWithTexture(
                        x0, y0, x1, y1, x2, y2, x3, y3,
                        depth,
                        face.getU0(), face.getV0_(), face.getU1(), face.getV1_(),
                        brightness,
                        batchIndex,
                        texWidth, texHeight,
                        faceIndex++
                ));
                minX = Math.min(minX, Math.min(Math.min(x0, x1), Math.min(x2, x3)));
                maxX = Math.max(maxX, Math.max(Math.max(x0, x1), Math.max(x2, x3)));
                minY = Math.min(minY, Math.min(Math.min(y0, y1), Math.min(y2, y3)));
                maxY = Math.max(maxY, Math.max(Math.max(y0, y1), Math.max(y2, y3)));
            }
        }

        // Stable sort: depth back-to-front, then by face index so coplanar faces (e.g. head/body seam) draw consistently
        projected.sort(Comparator.comparingDouble((ProjectedFaceWithTexture p) -> p.getDepth()).reversed()
                .thenComparingInt(ProjectedFaceWithTexture::getFaceIndex));
        double modelWidth = maxX - minX;
        double modelHeight = maxY - minY;
        if (modelWidth < 1) {
            modelWidth = 1;
        }
        if (modelHeight < 1) {
            modelHeight = 1;
        }
        double scale = Math.min(width / modelWidth, size / modelHeight);
        double offsetX = (width - modelWidth * scale) / 2 - minX * scale;
        double offsetY = maxY * scale;

        BufferedImage result = new BufferedImage(width, size, BufferedImage.TYPE_INT_ARGB);
        int[] outPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();

        // Preload texture pixels per batch (avoids repeated getRGB in loop)
        int[][] batchTexPixels = new int[batches.size()][];
        for (int i = 0; i < batches.size(); i++) {
            batchTexPixels[i] = QuadRasterizer.getTexturePixels(batches.get(i).getTexture());
        }

        for (ProjectedFaceWithTexture projectedFace : projected) {
            int[] texPixels = batchTexPixels[projectedFace.getTextureIndex()];
            int texWidth = projectedFace.getTexWidth();
            int texHeight = projectedFace.getTexHeight();

            double destX0 = projectedFace.getX0() * scale + offsetX;
            double destY0 = offsetY - projectedFace.getY0() * scale;
            double destX1 = projectedFace.getX1() * scale + offsetX;
            double destY1 = offsetY - projectedFace.getY1() * scale;
            double destX2 = projectedFace.getX2() * scale + offsetX;
            double destY2 = offsetY - projectedFace.getY2() * scale;

            // Same texture subrect as old Graphics2D path: floor(u0), floor(v0), ceil(u1)-floor(u0), ceil(v1)-floor(v0)
            int srcX1 = (int) Math.floor(projectedFace.getU0());
            int srcY1 = (int) Math.floor(projectedFace.getV0_());
            int srcX2 = (int) Math.ceil(projectedFace.getU1());
            int srcY2 = (int) Math.ceil(projectedFace.getV1_());
            srcX1 = Math.max(0, Math.min(srcX1, texWidth - 1));
            srcY1 = Math.max(0, Math.min(srcY1, texHeight - 1));
            srcX2 = Math.max(srcX1 + 1, Math.min(srcX2, texWidth));
            srcY2 = Math.max(srcY1 + 1, Math.min(srcY2, texHeight));
            int regionWidth = srcX2 - srcX1;
            int regionHeight = srcY2 - srcY1;
            if (regionWidth <= 0 || regionHeight <= 0) {
                continue;
            }

            QuadRasterizer.rasterizeQuad(
                    outPixels, width, size,
                    destX0, destY0, destX1, destY1, destX2, destY2,
                    srcX1, srcY1, regionWidth, regionHeight,
                    texPixels, texWidth, texHeight,
                    (float) projectedFace.getBrightness());
        }
        return result;
    }

    /**
     * Renders faces with a single texture. Convenience for skin-only rendering.
     *
     * @param texture the texture (e.g. 64x64 skin)
     * @param faces the list of textured faces
     * @param view view parameters
     * @param size output height in pixels
     * @return the rendered image
     */
    @NonNull
    public BufferedImage render(@NonNull BufferedImage texture, @NonNull List<Face> faces, @NonNull ViewParams view, int size) {
        return render(List.of(new TexturedFaces(texture, faces)), view, size);
    }

    /**
     * View parameters for the 3D isometric renderer.
     * The target is also used as the model rotation center.
     *
     * @author Braydon
     */
    @AllArgsConstructor @Getter
    public static final class ViewParams {
        /**
         * The camera eye position.
         */
        @NonNull private final Vector3 eye;

        /**
         * The target position (also the rotation center).
         */
        @NonNull private final Vector3 target;

        /**
         * The yaw rotation in degrees.
         */
        private final double yawDeg;

        /**
         * The pitch rotation in degrees.
         */
        private final double pitchDeg;

        /**
         * The aspect ratio (width / height).
         */
        private final double aspectRatio;
    }

    /**
     * Pairs a texture with its faces. Used for multi-texture rendering.
     *
     * @author Braydon
     */
    @AllArgsConstructor @Getter
    public static final class TexturedFaces {
        /**
         * The texture image.
         */
        @NonNull private final BufferedImage texture;

        /**
         * The list of faces using this texture.
         */
        @NonNull private final List<Face> faces;
    }

    /**
     * Internal class for projected face data with texture reference.
     *
     * @author Braydon
     */
    @AllArgsConstructor @Getter
    private static final class ProjectedFaceWithTexture {
        /**
         * The X coordinate of vertex 0.
         */
        private final double x0;

        /**
         * The Y coordinate of vertex 0.
         */
        private final double y0;

        /**
         * The X coordinate of vertex 1.
         */
        private final double x1;

        /**
         * The Y coordinate of vertex 1.
         */
        private final double y1;

        /**
         * The X coordinate of vertex 2.
         */
        private final double x2;

        /**
         * The Y coordinate of vertex 2.
         */
        private final double y2;

        /**
         * The X coordinate of vertex 3.
         */
        private final double x3;

        /**
         * The Y coordinate of vertex 3.
         */
        private final double y3;

        /**
         * The depth value for sorting.
         */
        private final double depth;

        /**
         * The U coordinate start.
         */
        private final double u0;

        /**
         * The V coordinate start.
         */
        private final double v0_;

        /**
         * The U coordinate end.
         */
        private final double u1;

        /**
         * The V coordinate end.
         */
        private final double v1_;

        /**
         * The brightness multiplier.
         */
        private final double brightness;

        /**
         * The index of the texture batch.
         */
        private final int textureIndex;

        /**
         * The texture width.
         */
        private final int texWidth;

        /**
         * The texture height.
         */
        private final int texHeight;

        /**
         * The face index for stable sorting.
         */
        private final int faceIndex;
    }
}
