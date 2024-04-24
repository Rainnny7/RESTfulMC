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
package cc.restfulmc.sdk.response;

import lombok.*;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * A representation of a player.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public final class Player extends CacheableResponse {
    /**
     * The unique id of this player.
     */
    @NonNull private final UUID uniqueId;

    /**
     * The username of this player.
     */
    @NonNull private final String username;

    /**
     * The skin of this player.
     */
    @NonNull private final Skin skin;

    /**
     * The cape of this player, null if none.
     */
    private final Cape cape;

    /**
     * The raw profile properties of this player.
     */
    @NonNull private final ProfileProperty[] properties;

    /**
     * The profile actions this player has, null if none.
     */
    private final ProfileAction[] profileActions;

    /**
     * Is this player legacy?
     * <p>
     * A "Legacy" player is a player that
     * has not yet migrated to a Mojang account.
     * </p>
     */
    private final boolean legacy;

    /**
     * A skin for a player.
     */
    @AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
    public static class Skin {
        /**
         * The texture URL of the skin.
         */
        @EqualsAndHashCode.Include @NonNull private final String url;

        /**
         * The model of this skin.
         */
        @NonNull private final SkinModel model;

        /**
         * Is this skin legacy?
         */
        private final boolean legacy;

        /**
         * URLs for the parts of this skin.
         */
        private final Map<SkinPart, String> parts;
    }

    /**
     * A model of a skin.
     */
    public enum SkinModel {
        DEFAULT, SLIM
    }

    /**
     * A part of a skin.
     */
    public enum SkinPart {
        // Overlays
        HEAD_OVERLAY_FACE,

        // Head
        HEAD_TOP,
        HEAD,
        FACE,
        HEAD_LEFT,
        HEAD_RIGHT,
        HEAD_BOTTOM,
        HEAD_BACK,

        // Body
        BODY_FLAT,
        BODY_FRONT,

        // Arms
        LEFT_ARM_TOP,
        RIGHT_ARM_TOP,

        LEFT_ARM_FRONT,
        RIGHT_ARM_FRONT,

        // Legs
        LEFT_LEG_FRONT,
        RIGHT_LEG_FRONT;
    }

    /**
     * A skin for a player.
     */
    @AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
    public static class Cape {
        /**
         * The texture URL of the cape.
         */
        @EqualsAndHashCode.Include @NonNull private final String url;
    }

    /**
     * A property of a player's profile.
     */
    @AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
    public static class ProfileProperty {
        /**
         * The name of this property.
         */
        @EqualsAndHashCode.Include @NonNull private final String name;

        /**
         * The value of this property.
         */
        @NonNull private final String value;

        /**
         * The Base64 signature of this property, null if none.
         */
        private String signature;

        /**
         * Get the decoded value
         * of this property.
         *
         * @return the decoded value
         */
        @NonNull
        public String getDecodedValue() {
            return new String(Base64.getDecoder().decode(value));
        }

        /**
         * Is this property signed?
         *
         * @return whether this property has a signature
         */
        public boolean isSigned() {
            return signature != null;
        }
    }

    /**
     * Actions that can be taken on a player's profile.
     */
    public enum ProfileAction {
        /**
         * The player is required to change their
         * username before accessing Multiplayer.
         */
        FORCED_NAME_CHANGE,

        /**
         * The player is using a banned skin.
         */
        USING_BANNED_SKIN
    }
}