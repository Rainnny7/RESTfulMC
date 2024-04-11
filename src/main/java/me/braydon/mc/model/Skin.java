/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.*;
import me.braydon.mc.config.AppConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A skin for a {@link Player}.
 *
 * @author Braydon
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @Setter @Getter @ToString
public final class Skin {
    public static final Skin DEFAULT_STEVE = new Skin("http://textures.minecraft.net/texture/60a5bd016b3c9a1b9272e4929e30827a67be4ebb219017adbbc4a4d22ebd5b1", Model.DEFAULT);

    /**
     * The texture URL of this skin.
     */
    @NonNull private final String url;

    /**
     * The model of this skin.
     */
    @NonNull private final Model model;

    /**
     * URLs to the parts of this skin.
     */
    @NonNull @JsonProperty("parts") private Map<String, String> partUrls = new HashMap<>();

    /**
     * Populate the part URLs for this skin.
     *
     * @param playerUuid the UUID of the player
     * @return the skin
     */
    @NonNull
    public Skin populatePartUrls(@NonNull String playerUuid) {
        Consumer<IPart> addPart = part -> {
            partUrls.put(part.name(), AppConfig.INSTANCE.getServerPublicUrl() + "/player/" + part.name().toLowerCase() + "/" + playerUuid + ".png");
        };
        for (Part part : Part.values()) {
            addPart.accept(part);
        }
        for (IsometricPart part : IsometricPart.values()) {
            addPart.accept(part);
        }
        return this;
    }

    /**
     * Build a skin from the given Json object.
     *
     * @param jsonObject the json object to build from
     * @return the built skin
     */
    public static Skin fromJsonObject(JsonObject jsonObject) {
        if (jsonObject == null) { // No object to parse
            return null;
        }
        Model model = Model.DEFAULT; // The skin model

        JsonObject metadataJsonObject = jsonObject.getAsJsonObject("metadata");
        if (metadataJsonObject != null) { // Parse the skin model
            model = Model.valueOf(metadataJsonObject.get("model").getAsString().toUpperCase());
        }
        return new Skin(jsonObject.get("url").getAsString(), model);
    }

    /**
     * Possible models for a skin.
     */
    public enum Model {
        DEFAULT, SLIM
    }

    /**
     * Represents a part of a skin.
     */
    public interface IPart {
        /**
         * Get the name of this part.
         *
         * @return the part name
         */
        @NonNull String name();
    }

    /**
     * The part of a skin.
     */
    @AllArgsConstructor @RequiredArgsConstructor @Getter @ToString
    public enum Part implements IPart {
        // Head
        HEAD_TOP(new Coordinates(8, 0), 8, 8),
        FACE(new Coordinates(8, 8), 8, 8),
        HEAD_LEFT(new Coordinates(0, 8), 8, 8),
        HEAD_RIGHT(new Coordinates(16, 8), 8, 8),
        HEAD_BOTTOM(new Coordinates(16, 0), 8, 8),
        HEAD_BACK(new Coordinates(24, 8), 8, 8);

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

    public enum IsometricPart implements IPart {
        HEAD
    }
}