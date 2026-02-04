package cc.restfulmc.api.common.renderer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Coordinates and geometry for the Minecraft player model.
 * <p>
 * Vanilla skin part UVs, model box definitions, and legacy skin upgrade mappings.
 * </p>
 *
 * @author Braydon
 */
public final class PlayerModelCoordinates {
    /**
     * Vanilla skin part definitions with coordinates and dimensions.
     *
     * @author Braydon
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

        /**
         * The UV coordinates.
         */
        private final Coordinates coordinates;

        /**
         * The width.
         */
        private final int width;

        /**
         * The height.
         */
        private final int height;

        /**
         * The overlay parts, if any.
         */
        private final Vanilla[] overlays;

        Vanilla(Coordinates coordinates, int width, int height, Vanilla... overlays) {
            this.coordinates = coordinates;
            this.width = width;
            this.height = height;
            this.overlays = overlays;
        }

        /**
         * Checks if this part is a front arm.
         *
         * @return true if this is a front arm part
         */
        public boolean isFrontArm() {
            return this == LEFT_ARM_FRONT || this == RIGHT_ARM_FRONT
                    || this == LEFT_ARM_OVERLAY_FRONT || this == RIGHT_ARM_OVERLAY_FRONT;
        }

        /**
         * UV coordinates.
         *
         * @author Braydon
         */
        @AllArgsConstructor @Getter
        public static final class Coordinates {
            /**
             * The X coordinate.
             */
            private final int x;

            /**
             * The Y coordinate.
             */
            private final int y;
        }
    }

    /**
     * Model box definitions for the 3D player model.
     * <p>
     * Maps each body part to its base and overlay texture regions and box dimensions.
     * </p>
     *
     * @author Braydon
     */
    @AllArgsConstructor
    public enum ModelBox {
        HEAD(Vanilla.FACE, Vanilla.HEAD_OVERLAY_FACE, 8),
        BODY(Vanilla.BODY_FRONT, Vanilla.BODY_OVERLAY_FRONT, 4),
        LEFT_ARM(Vanilla.LEFT_ARM_FRONT, Vanilla.LEFT_ARM_OVERLAY_FRONT, 4),
        RIGHT_ARM(Vanilla.RIGHT_ARM_FRONT, Vanilla.RIGHT_ARM_OVERLAY_FRONT, 4),
        LEFT_LEG(Vanilla.LEFT_LEG_FRONT, Vanilla.LEFT_LEG_OVERLAY_FRONT, 4),
        RIGHT_LEG(Vanilla.RIGHT_LEG_FRONT, Vanilla.RIGHT_LEG_OVERLAY_FRONT, 4);

        /**
         * The base part.
         */
        private final Vanilla basePart;

        /**
         * The overlay part.
         */
        private final Vanilla overlayPart;

        /**
         * The box depth.
         */
        private final int depth;

        /**
         * Gets the base UV coordinates.
         *
         * @param slim whether to use slim arm dimensions
         * @return the UV array [x, y, sizeX, sizeY, sizeZ]
         */
        public int[] getBaseUv(boolean slim) {
            return getUv(basePart, slim, depth);
        }

        /**
         * Gets the overlay UV coordinates.
         *
         * @param slim whether to use slim arm dimensions
         * @return the UV array [x, y, sizeX, sizeY, sizeZ]
         */
        public int[] getOverlayUv(boolean slim) {
            return getUv(overlayPart, slim, depth);
        }

        /**
         * Gets UV coordinates for a part.
         *
         * @param part the part
         * @param slim whether to use slim arm dimensions
         * @param sizeZ the depth
         * @return the UV array
         */
        private static int[] getUv(Vanilla part, boolean slim, int sizeZ) {
            int width = part.getWidth();
            if (slim && part.isFrontArm()) {
                width--;
            }
            return new int[]{
                    part.getCoordinates().getX(),
                    part.getCoordinates().getY(),
                    width,
                    part.getHeight(),
                    sizeZ
            };
        }
    }

    /**
     * Legacy 64x32 skin layout coordinates and upgrade mappings for converting
     * to modern 64x64 format.
     * <p>
     * Legacy skins have no overlays - only base layer.
     * Left arm/leg are created by mirroring the right arm/leg.
     * Each copy rect is {dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2}.
     * </p>
     *
     * @author Braydon
     */
    public static final class LegacyUpgrade {
        /**
         * Copy rectangles for left leg from right leg (mirrored).
         */
        public static final int[][] LEFT_LEG_COPIES = {
                {24, 48, 20, 52, 4, 16, 8, 20},
                {28, 48, 24, 52, 8, 16, 12, 20},
                {20, 52, 16, 64, 8, 20, 12, 32},
                {24, 52, 20, 64, 4, 20, 8, 32},
                {28, 52, 24, 64, 0, 20, 4, 32},
                {32, 52, 28, 64, 12, 20, 16, 32},
        };

        /**
         * Copy rectangles for left arm from right arm (mirrored).
         */
        public static final int[][] LEFT_ARM_COPIES = {
                {16, 32, 0, 48, 40, 16, 56, 32},
                {40, 48, 36, 52, 44, 16, 48, 20},
                {44, 48, 40, 52, 48, 16, 52, 20},
                {36, 52, 32, 64, 48, 20, 52, 32},
                {40, 52, 36, 64, 44, 20, 48, 32},
                {44, 52, 40, 64, 40, 20, 44, 32},
                {48, 52, 44, 64, 52, 20, 56, 32},
        };

        /**
         * Regions to clear for overlay layers (since legacy has none).
         */
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
