package cc.restfulmc.api.common.renderer.model;

import lombok.Getter;

/**
 * Coordinates and geometry for the Minecraft player model.
 * Vanilla skin part UVs, model box definitions, and legacy skin upgrade mappings.
 */
public final class PlayerModelCoordinates {
    /**
     * Vanilla skin part definitions with coordinates and dimensions.
     */
    @Getter
    public enum Vanilla {
        // Head overlays
        HEAD_OVERLAY_TOP(new Coordinates(40, 0), 8, 8),
        HEAD_OVERLAY_FACE(new Coordinates(40, 8), 8, 8),
        HEAD_OVERLAY_LEFT(new Coordinates(32, 8), 8, 8),
        HEAD_OVERLAY_RIGHT(new Coordinates(48, 8), 8, 8),
        HEAD_OVERLAY_BACK(new Coordinates(56, 8), 8, 8),
        HEAD_OVERLAY_BOTTOM(new Coordinates(48, 0), 8, 8),

        // Body overlays
        BODY_OVERLAY_FRONT(new Coordinates(20, 36), 8, 12),
        BODY_OVERLAY_TOP(new Coordinates(20, 32), 8, 4),
        BODY_OVERLAY_LEFT(new Coordinates(36, 36), 4, 12),
        BODY_OVERLAY_RIGHT(new Coordinates(28, 36), 4, 12),
        BODY_OVERLAY_BACK(new Coordinates(44, 36), 8, 12),

        // Arm overlays
        LEFT_ARM_OVERLAY_FRONT(new Coordinates(52, 52), 4, 12),
        LEFT_ARM_OVERLAY_TOP(new Coordinates(52, 48), 4, 4),
        RIGHT_ARM_OVERLAY_FRONT(new Coordinates(44, 36), 4, 12),
        RIGHT_ARM_OVERLAY_TOP(new Coordinates(44, 48), 4, 4),

        // Leg overlays
        LEFT_LEG_OVERLAY_FRONT(new Coordinates(4, 52), 4, 12),
        LEFT_LEG_OVERLAY_TOP(new Coordinates(4, 48), 4, 4),
        RIGHT_LEG_OVERLAY_FRONT(new Coordinates(4, 36), 4, 12),
        RIGHT_LEG_OVERLAY_TOP(new Coordinates(4, 32), 4, 4),

        // Head
        HEAD_TOP(new Coordinates(8, 0), 8, 8, HEAD_OVERLAY_TOP),
        FACE(new Coordinates(8, 8), 8, 8, HEAD_OVERLAY_FACE),
        HEAD_LEFT(new Coordinates(0, 8), 8, 8, HEAD_OVERLAY_LEFT),
        HEAD_RIGHT(new Coordinates(16, 8), 8, 8, HEAD_OVERLAY_RIGHT),
        HEAD_BOTTOM(new Coordinates(16, 0), 8, 8, HEAD_OVERLAY_BOTTOM),
        HEAD_BACK(new Coordinates(24, 8), 8, 8, HEAD_OVERLAY_BACK),

        // Body
        BODY_FRONT(new Coordinates(20, 20), 8, 12, BODY_OVERLAY_FRONT),
        BODY_TOP(new Coordinates(20, 16), 8, 4, BODY_OVERLAY_TOP),
        BODY_LEFT(new Coordinates(36, 20), 4, 12, BODY_OVERLAY_LEFT),
        BODY_RIGHT(new Coordinates(28, 20), 4, 12, BODY_OVERLAY_RIGHT),
        BODY_BACK(new Coordinates(44, 20), 8, 12, BODY_OVERLAY_BACK),

        // Arms
        LEFT_ARM_TOP(new Coordinates(36, 48), 4, 4, LEFT_ARM_OVERLAY_TOP),
        RIGHT_ARM_TOP(new Coordinates(44, 16), 4, 4, RIGHT_ARM_OVERLAY_TOP),
        LEFT_ARM_FRONT(new Coordinates(36, 52), 4, 12, LEFT_ARM_OVERLAY_FRONT),
        RIGHT_ARM_FRONT(new Coordinates(44, 20), 4, 12, RIGHT_ARM_OVERLAY_FRONT),

        // Legs
        LEFT_LEG_TOP(new Coordinates(20, 48), 4, 4, LEFT_LEG_OVERLAY_TOP),
        RIGHT_LEG_TOP(new Coordinates(4, 16), 4, 4, RIGHT_LEG_OVERLAY_TOP),
        LEFT_LEG_FRONT(new Coordinates(20, 52), 4, 12, LEFT_LEG_OVERLAY_FRONT),
        RIGHT_LEG_FRONT(new Coordinates(4, 20), 4, 12, RIGHT_LEG_OVERLAY_FRONT);

        private final Coordinates coordinates;
        private final int width;
        private final int height;
        private final Vanilla[] overlays;

        Vanilla(Coordinates coordinates, int width, int height, Vanilla... overlays) {
            this.coordinates = coordinates;
            this.width = width;
            this.height = height;
            this.overlays = overlays;
        }

        public boolean isFrontArm() {
            return this == LEFT_ARM_FRONT || this == RIGHT_ARM_FRONT
                    || this == LEFT_ARM_OVERLAY_FRONT || this == RIGHT_ARM_OVERLAY_FRONT;
        }

        public record Coordinates(int x, int y) {}
    }

    /**
     * Model box definitions for the 3D player model. Maps each body part to its
     * base and overlay texture regions and box dimensions.
     */
    public enum ModelBox {
        HEAD(Vanilla.FACE, Vanilla.HEAD_OVERLAY_FACE, 8),
        BODY(Vanilla.BODY_FRONT, Vanilla.BODY_OVERLAY_FRONT, 4),
        LEFT_ARM(Vanilla.LEFT_ARM_FRONT, Vanilla.LEFT_ARM_OVERLAY_FRONT, 4),
        RIGHT_ARM(Vanilla.RIGHT_ARM_FRONT, Vanilla.RIGHT_ARM_OVERLAY_FRONT, 4),
        LEFT_LEG(Vanilla.LEFT_LEG_FRONT, Vanilla.LEFT_LEG_OVERLAY_FRONT, 4),
        RIGHT_LEG(Vanilla.RIGHT_LEG_FRONT, Vanilla.RIGHT_LEG_OVERLAY_FRONT, 4);

        private final Vanilla basePart;
        private final Vanilla overlayPart;
        private final int depth;

        ModelBox(Vanilla basePart, Vanilla overlayPart, int depth) {
            this.basePart = basePart;
            this.overlayPart = overlayPart;
            this.depth = depth;
        }

        public int[] getBaseUv(boolean slim) {
            return getUv(basePart, slim, depth);
        }

        public int[] getOverlayUv(boolean slim) {
            return getUv(overlayPart, slim, depth);
        }

        private static int[] getUv(Vanilla part, boolean slim, int sizeZ) {
            int w = part.getWidth();
            if (slim && part.isFrontArm()) {
                w--;
            }
            return new int[]{
                    part.getCoordinates().x(),
                    part.getCoordinates().y(),
                    w,
                    part.getHeight(),
                    sizeZ
            };
        }
    }

    /**
     * Legacy 64×32 skin layout coordinates and upgrade mappings for converting
     * to modern 64×64 format. Legacy skins have no overlays — only base layer.
     * Left arm/leg are created by mirroring the right arm/leg.
     * <p>
     * Each copy rect is {@code {dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2}}.
     * </p>
     */
    public static final class LegacyUpgrade {
        public static final int[][] LEFT_LEG_COPIES = {
                {24, 48, 20, 52, 4, 16, 8, 20},
                {28, 48, 24, 52, 8, 16, 12, 20},
                {20, 52, 16, 64, 8, 20, 12, 32},
                {24, 52, 20, 64, 4, 20, 8, 32},
                {28, 52, 24, 64, 0, 20, 4, 32},
                {32, 52, 28, 64, 12, 20, 16, 32},
        };

        public static final int[][] LEFT_ARM_COPIES = {
                {16, 32, 0, 48, 40, 16, 56, 32},
                {40, 48, 36, 52, 44, 16, 48, 20},
                {44, 48, 40, 52, 48, 16, 52, 20},
                {36, 52, 32, 64, 48, 20, 52, 32},
                {40, 52, 36, 64, 44, 20, 48, 32},
                {44, 52, 40, 64, 40, 20, 44, 32},
                {48, 52, 44, 64, 52, 20, 56, 32},
        };

        public static final int[][] CLEAR_OVERLAYS = {
                {32, 0, 64, 16},
                {0, 32, 16, 48},
                {16, 32, 40, 48},
                {40, 32, 56, 48},
                {0, 48, 16, 64},
                {48, 48, 64, 64},
        };
    }

}