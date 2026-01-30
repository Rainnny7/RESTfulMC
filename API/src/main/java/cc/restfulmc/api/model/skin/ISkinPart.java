package cc.restfulmc.api.model.skin;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.common.renderer.impl.BodySkinPartRenderer;
import cc.restfulmc.api.common.renderer.impl.IsometricHeadSkinPartRenderer;
import cc.restfulmc.api.common.renderer.impl.VanillaSkinPartRenderer;
import lombok.*;

import java.awt.image.BufferedImage;

/**
 * A part of a {@link Skin}.
 *
 * @author Braydon
 */
public interface ISkinPart {
    Enum<?>[][] TYPES = { Vanilla.values(), Custom.values() };

    /**
     * Get the name of this part.
     *
     * @return the part name
     */
    @NonNull String name();

    /**
     * Render a part of a skin.
     *
     * @param skin     the skin to render the part for
     * @param overlays whether to render overlays
     * @param size     the size to scale the skin part to
     * @return the rendered skin part
     */
    @NonNull BufferedImage render(@NonNull Skin skin, boolean overlays, int size);

    /**
     * Get a skin part by the given name.
     *
     * @param name the name of the part
     * @return the part, null if none
     */
    static ISkinPart getByName(@NonNull String name) {
        name = name.toUpperCase();
        for (Enum<?>[] type : TYPES) {
            for (Enum<?> part : type) {
                if (!part.name().equals(name)) {
                    continue;
                }
                return (ISkinPart) part;
            }
        }
        return null;
    }

    /**
     * A part of a Vanilla skin texture.
     */
    @RequiredArgsConstructor @Getter @ToString
    enum Vanilla implements ISkinPart {
        // Overlays
        HEAD_OVERLAY_FACE(new Coordinates(40, 8), 8, 8),

        // Head
        HEAD_TOP(new Coordinates(8, 0), 8, 8),
        FACE(new Coordinates(8, 8), 8, 8, HEAD_OVERLAY_FACE),
        HEAD_LEFT(new Coordinates(0, 8), 8, 8),
        HEAD_RIGHT(new Coordinates(16, 8), 8, 8),
        HEAD_BOTTOM(new Coordinates(16, 0), 8, 8),
        HEAD_BACK(new Coordinates(24, 8), 8, 8),

        // Body
        BODY_FRONT(new Coordinates(20, 20), 8, 12),

        // Arms
        LEFT_ARM_TOP(new Coordinates(36, 48), 4, 4),
        RIGHT_ARM_TOP(new Coordinates(44, 16), 4, 4),

        LEFT_ARM_FRONT(new Coordinates(44, 20), 4, 12),
        RIGHT_ARM_FRONT(new Coordinates(36, 52), new LegacyCoordinates(44, 20, true), 4, 12),

        // Legs
        LEFT_LEG_FRONT(new Coordinates(4, 20), 4, 12), // Front
        RIGHT_LEG_FRONT(new Coordinates(20, 52), new LegacyCoordinates(4, 20, true), 4, 12); // Front

        /**
         * The coordinates of this part.
         */
        @NonNull private final Coordinates coordinates;

        /**
         * The legacy coordinates of this part.
         * <p>
         * This is for older skin textures
         * that use different positions.
         * </p>
         */
        private LegacyCoordinates legacyCoordinates;

        /**
         * The size of this part.
         */
        private final int width, height;

        /**
         * The overlay parts this part has.
         */
        private Vanilla[] overlays;

        Vanilla(@NonNull Coordinates coordinates, int width, int height, Vanilla... overlays) {
            this(coordinates, null, width, height, overlays);
        }

        Vanilla(@NonNull Coordinates coordinates, LegacyCoordinates legacyCoordinates, int width, int height, Vanilla... overlays) {
            this.coordinates = coordinates;
            this.legacyCoordinates = legacyCoordinates;
            this.width = width;
            this.height = height;
            this.overlays = overlays;
        }

        /**
         * Render a part of a skin.
         *
         * @param skin     the skin to render the part for
         * @param overlays whether to render overlays
         * @param size     the size to scale the skin part to
         * @return the rendered skin part
         */
        @Override @NonNull
        public BufferedImage render(@NonNull Skin skin, boolean overlays, int size) {
            return VanillaSkinPartRenderer.INSTANCE.render(skin, this, overlays, size);
        }

        /**
         * Is this part a front arm?
         *
         * @return whether this part is a front arm
         */
        public boolean isFrontArm() {
            return this == LEFT_ARM_FRONT || this == RIGHT_ARM_FRONT;
        }

        /**
         * Does this part have legacy coordinates?
         *
         * @return whether this part has legacy coordinates
         */
        public boolean hasLegacyCoordinates() {
            return legacyCoordinates != null;
        }

        /**
         * Coordinates of a part of a skin.
         */
        @AllArgsConstructor @Getter @ToString
        public static class Coordinates {
            /**
             * The X coordinate.
             */
            private final int x;

            /**
             * The Y coordinate.
             */
            private final int y;
        }

        /**
         * Legacy coordinates of a part of a skin.
         */
        @Getter @ToString
        public static class LegacyCoordinates extends Coordinates {
            /**
             * Whether the part at these coordinates is flipped.
             */
            private final boolean flipped;

            public LegacyCoordinates(int x, int y) {
                this(x, y, false);
            }

            public LegacyCoordinates(int x, int y, boolean flipped) {
                super(x, y);
                this.flipped = flipped;
            }
        }
    }

    /**
     * A custom part of a skin.
     */
    @AllArgsConstructor @Getter
    enum Custom implements ISkinPart {
        HEAD(IsometricHeadSkinPartRenderer.INSTANCE),
        BODY_FLAT(BodySkinPartRenderer.INSTANCE);

        /**
         * The custom renderer to use for this part.
         */
        @NonNull private final SkinRenderer<Custom> renderer;

        /**
         * Render a part of a skin.
         *
         * @param skin     the skin to render the part for
         * @param overlays whether to render overlays
         * @param size     the size to scale the skin part to
         * @return the rendered skin part
         */
        @Override @NonNull
        public BufferedImage render(@NonNull Skin skin, boolean overlays, int size) {
            return renderer.render(skin, this, overlays, size);
        }
    }
}