package cc.restfulmc.api.common.renderer.raster;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Fast software rasterizer for textured quads.
 * <p>
 * Scanline-based: for each row, intersect with parallelogram edges,
 * interpolate (u,v) along the span. Matches Graphics2D.drawImage(AffineTransform)
 * output with less work than per-pixel inverse.
 * </p>
 *
 * @author Braydon
 */
@UtilityClass
public final class QuadRasterizer {
    /**
     * Rasterizes a parallelogram like Graphics2D.drawImage(subimage, AffineTransform).
     * <p>
     * Parallelogram: (dx0,dy0)=texture(texX0,texY0), (dx1,dy1)=texture(texX0+tw,texY0),
     * (dx2,dy2)=texture(texX0,texY0+th).
     * Scanline: for each y, intersect with edges to get left/right x and (u,v),
     * then interpolate along the span.
     * </p>
     *
     * @param outPixels the output pixel array
     * @param outWidth the output width
     * @param outHeight the output height
     * @param dx0 destination X for top-left
     * @param dy0 destination Y for top-left
     * @param dx1 destination X for top-right
     * @param dy1 destination Y for top-right
     * @param dx2 destination X for bottom-left
     * @param dy2 destination Y for bottom-left
     * @param texX0 texture X offset
     * @param texY0 texture Y offset
     * @param texWidth texture region width
     * @param texHeight texture region height
     * @param texPixels texture pixel array
     * @param texTotalWidth texture total width
     * @param texTotalHeight texture total height
     * @param brightness brightness multiplier
     */
    public static void rasterizeQuad(int[] outPixels, int outWidth, int outHeight,
                                     double dx0, double dy0, double dx1, double dy1, double dx2, double dy2,
                                     int texX0, int texY0, int texWidth, int texHeight,
                                     int[] texPixels, int texTotalWidth, int texTotalHeight,
                                     float brightness) {

        if (texWidth <= 0 || texHeight <= 0) {
            return;
        }

        double dx3 = dx1 + dx2 - dx0;
        double dy3 = dy1 + dy2 - dy0;
        // Vertices 0,1,2,3 with subimage coords (0,0), (tw,0), (0,th), (tw,th)
        double[] edgeX = {dx0, dx1, dx3, dx2};
        double[] edgeY = {dy0, dy1, dy3, dy2};
        double[] edgeU = {0, texWidth, texWidth, 0};
        double[] edgeV = {0, 0, texHeight, texHeight};

        int yMin = (int) Math.ceil(Math.min(Math.min(edgeY[0], edgeY[1]), Math.min(edgeY[2], edgeY[3])) - 0.5);
        int yMax = (int) Math.floor(Math.max(Math.max(edgeY[0], edgeY[1]), Math.max(edgeY[2], edgeY[3])) - 0.5);
        yMin = Math.max(0, yMin);
        yMax = Math.min(outHeight - 1, yMax);
        if (yMin > yMax) {
            return;
        }

        final double epsilon = 1e-6;
        for (int y = yMin; y <= yMax; y++) {
            double pixelY = y + 0.5;
            double xMin = Double.POSITIVE_INFINITY;
            double xMax = Double.NEGATIVE_INFINITY;
            double uLeft = 0, vLeft = 0, uRight = 0, vRight = 0;

            for (int edge = 0; edge < 4; edge++) {
                int nextEdge = (edge + 1) % 4;
                double yA = edgeY[edge];
                double yB = edgeY[nextEdge];
                if (Math.abs(yB - yA) < 1e-9) {
                    continue;
                }
                if (pixelY < Math.min(yA, yB) - epsilon || pixelY > Math.max(yA, yB) + epsilon) {
                    continue;
                }

                double t = (pixelY - yA) / (yB - yA);
                double x = edgeX[edge] + t * (edgeX[nextEdge] - edgeX[edge]);
                double u = edgeU[edge] + t * (edgeU[nextEdge] - edgeU[edge]);
                double v = edgeV[edge] + t * (edgeV[nextEdge] - edgeV[edge]);

                if (x < xMin) {
                    xMin = x;
                    uLeft = u;
                    vLeft = v;
                }
                if (x > xMax) {
                    xMax = x;
                    uRight = u;
                    vRight = v;
                }
            }

            if (xMin > xMax) {
                continue;
            }

            int xStart = Math.max(0, (int) Math.ceil(xMin - 0.5));
            int xEnd = Math.min(outWidth - 1, (int) Math.floor(xMax - 0.5));
            if (xStart > xEnd) {
                int xMid = (int) Math.round((xMin + xMax) / 2);
                if (xMid >= 0 && xMid < outWidth) {
                    xStart = xMid;
                    xEnd = xMid;
                } else {
                    continue;
                }
            }

            double deltaX = xMax - xMin;
            double deltaU = (deltaX > 1e-9) ? (uRight - uLeft) / deltaX : 0;
            double deltaV = (deltaX > 1e-9) ? (vRight - vLeft) / deltaX : 0;
            double u = uLeft + (xStart - xMin + 0.5) * deltaU;
            double v = vLeft + (xStart - xMin + 0.5) * deltaV;

            int rowOffset = y * outWidth;
            for (int x = xStart; x <= xEnd; x++) {
                int texX = texX0 + (int) Math.floor(u);
                int texY = texY0 + (int) Math.floor(v);
                texX = Math.max(0, Math.min(texX, texTotalWidth - 1));
                texY = Math.max(0, Math.min(texY, texTotalHeight - 1));

                int pixel = texPixels[texY * texTotalWidth + texX];
                int alpha = (pixel >> 24) & 0xFF;
                if (alpha == 0) {
                    u += deltaU;
                    v += deltaV;
                    continue;
                }

                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                if (brightness != 1.0f) {
                    red = (int) (red * brightness);
                    green = (int) (green * brightness);
                    blue = (int) (blue * brightness);
                    red = Math.min(255, Math.max(0, red));
                    green = Math.min(255, Math.max(0, green));
                    blue = Math.min(255, Math.max(0, blue));
                }

                int dstIdx = rowOffset + x;
                int dst = outPixels[dstIdx];
                if (alpha >= 254) {
                    outPixels[dstIdx] = (alpha << 24) | (red << 16) | (green << 8) | blue;
                } else {
                    int dstAlpha = (dst >> 24) & 0xFF;
                    int dstRed = (dst >> 16) & 0xFF;
                    int dstGreen = (dst >> 8) & 0xFF;
                    int dstBlue = dst & 0xFF;
                    int invSrcAlpha = 255 - alpha;
                    red = (red * alpha + dstRed * invSrcAlpha) / 255;
                    green = (green * alpha + dstGreen * invSrcAlpha) / 255;
                    blue = (blue * alpha + dstBlue * invSrcAlpha) / 255;
                    alpha = alpha + (255 - alpha) * dstAlpha / 255;
                    outPixels[dstIdx] = (Math.min(255, alpha) << 24) | (Math.min(255, red) << 16) | (Math.min(255, green) << 8) | Math.min(255, blue);
                }

                u += deltaU;
                v += deltaV;
            }
        }
    }

    /**
     * Gets texture pixels as int[]. Uses DataBufferInt when available for zero-copy.
     *
     * @param texture the texture image
     * @return the pixel array
     */
    @NonNull
    public static int[] getTexturePixels(@NonNull BufferedImage texture) {
        if (texture.getRaster().getDataBuffer() instanceof DataBufferInt dataBuffer) {
            return dataBuffer.getData();
        }
        // Fallback: copy via getRGB
        int width = texture.getWidth();
        int height = texture.getHeight();
        return texture.getRGB(0, 0, width, height, null, 0, width);
    }
}
