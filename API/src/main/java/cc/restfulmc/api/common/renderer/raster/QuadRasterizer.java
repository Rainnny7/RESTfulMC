package cc.restfulmc.api.common.renderer.raster;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Fast software rasterizer for textured quads. Scanline-based: for each row,
 * intersect with parallelogram edges, interpolate (u,v) along the span. Matches
 * Graphics2D.drawImage( AffineTransform ) output with less work than per-pixel inverse.
 */
public final class QuadRasterizer {
    /**
     * Rasterize a parallelogram like Graphics2D.drawImage( subimage, AffineTransform ).
     * Parallelogram: (dx0,dy0)=texture(texX0,texY0), (dx1,dy1)=texture(texX0+tw,texY0), (dx2,dy2)=texture(texX0,texY0+th).
     * Scanline: for each y, intersect with edges to get left/right x and (u,v), then interpolate along the span.
     */
    public static void rasterizeQuad(
            int[] outPixels, int outW, int outH,
            double dx0, double dy0, double dx1, double dy1, double dx2, double dy2,
            int texX0, int texY0, int tw, int th,
            int[] texPixels, int texW, int texH,
            float brightness) {

        if (tw <= 0 || th <= 0) return;

        double dx3 = dx1 + dx2 - dx0;
        double dy3 = dy1 + dy2 - dy0;
        // Vertices 0,1,2,3 with subimage coords (0,0), (tw,0), (0,th), (tw,th)
        double[] ex = {dx0, dx1, dx3, dx2};
        double[] ey = {dy0, dy1, dy3, dy2};
        double[] eu = {0, tw, tw, 0};
        double[] ev = {0, 0, th, th};

        int yMin = (int) Math.ceil(Math.min(Math.min(ey[0], ey[1]), Math.min(ey[2], ey[3])) - 0.5);
        int yMax = (int) Math.floor(Math.max(Math.max(ey[0], ey[1]), Math.max(ey[2], ey[3])) - 0.5);
        yMin = Math.max(0, yMin);
        yMax = Math.min(outH - 1, yMax);
        if (yMin > yMax) return;

        final double eps = 1e-6;
        for (int y = yMin; y <= yMax; y++) {
            double py = y + 0.5;
            double xMin = Double.POSITIVE_INFINITY;
            double xMax = Double.NEGATIVE_INFINITY;
            double uLeft = 0, vLeft = 0, uRight = 0, vRight = 0;

            for (int e = 0; e < 4; e++) {
                int e1 = (e + 1) % 4;
                double yA = ey[e], yB = ey[e1];
                if (Math.abs(yB - yA) < 1e-9) continue;
                if (py < Math.min(yA, yB) - eps || py > Math.max(yA, yB) + eps) continue;

                double t = (py - yA) / (yB - yA);
                double x = ex[e] + t * (ex[e1] - ex[e]);
                double u = eu[e] + t * (eu[e1] - eu[e]);
                double v = ev[e] + t * (ev[e1] - ev[e]);

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

            if (xMin > xMax) continue;

            int xStart = Math.max(0, (int) Math.ceil(xMin - 0.5));
            int xEnd = Math.min(outW - 1, (int) Math.floor(xMax - 0.5));
            if (xStart > xEnd) {
                int xMid = (int) Math.round((xMin + xMax) / 2);
                if (xMid >= 0 && xMid < outW) {
                    xStart = xMid;
                    xEnd = xMid;
                } else continue;
            }

            double dx = xMax - xMin;
            double du = (dx > 1e-9) ? (uRight - uLeft) / dx : 0;
            double dv = (dx > 1e-9) ? (vRight - vLeft) / dx : 0;
            double u = uLeft + (xStart - xMin + 0.5) * du;
            double v = vLeft + (xStart - xMin + 0.5) * dv;

            int rowOffset = y * outW;
            for (int x = xStart; x <= xEnd; x++) {
                int texX = texX0 + (int) Math.floor(u);
                int texY = texY0 + (int) Math.floor(v);
                texX = Math.max(0, Math.min(texX, texW - 1));
                texY = Math.max(0, Math.min(texY, texH - 1));

                int pixel = texPixels[texY * texW + texX];
                int a_ = (pixel >> 24) & 0xFF;
                if (a_ == 0) {
                    u += du;
                    v += dv;
                    continue;
                }

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                if (brightness != 1.0f) {
                    r = (int) (r * brightness);
                    g = (int) (g * brightness);
                    b = (int) (b * brightness);
                    r = Math.min(255, Math.max(0, r));
                    g = Math.min(255, Math.max(0, g));
                    b = Math.min(255, Math.max(0, b));
                }

                int dstIdx = rowOffset + x;
                int dst = outPixels[dstIdx];
                if (a_ >= 254) {
                    outPixels[dstIdx] = (a_ << 24) | (r << 16) | (g << 8) | b;
                } else {
                    int da = (dst >> 24) & 0xFF;
                    int dr = (dst >> 16) & 0xFF;
                    int dg = (dst >> 8) & 0xFF;
                    int db = dst & 0xFF;
                    int invSa = 255 - a_;
                    r = (r * a_ + dr * invSa) / 255;
                    g = (g * a_ + dg * invSa) / 255;
                    b = (b * a_ + db * invSa) / 255;
                    a_ = a_ + (255 - a_) * da / 255;
                    outPixels[dstIdx] = (Math.min(255, a_) << 24) | (Math.min(255, r) << 16) | (Math.min(255, g) << 8) | Math.min(255, b);
                }

                u += du;
                v += dv;
            }
        }
    }

    /**
     * Get texture pixels as int[]. Uses DataBufferInt when available for zero-copy.
     */
    public static int[] getTexturePixels(BufferedImage texture) {
        if (texture.getRaster().getDataBuffer() instanceof DataBufferInt db) {
            return db.getData();
        }
        // Fallback: copy via getRGB
        int w = texture.getWidth();
        int h = texture.getHeight();
        return texture.getRGB(0, 0, w, h, null, 0, w);
    }

}
