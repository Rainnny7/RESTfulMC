package me.braydon.mc.model.token;

import lombok.*;
import me.braydon.mc.model.ProfileAction;

/**
 * A token representing a Mojang user profile.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Mojang_API#UUID_to_Profile_and_Skin.2FCape">Mojang API</a>
 */
@NoArgsConstructor @Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public final class MojangProfileToken {
    /**
     * The id of the profile.
     */
    @EqualsAndHashCode.Include @NonNull private String id;

    /**
     * The name of the profile.
     */
    @NonNull private String name;

    /**
     * The properties of the profile.
     */
    @NonNull private ProfileProperty[] properties;

    /**
     * The actions this profile has.
     */
    @NonNull private ProfileAction[] profileActions;

    /**
     * A property of a Mojang profile.
     */
    @NoArgsConstructor @Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
    public static class ProfileProperty {
        /**
         * The name of this property.
         */
        @EqualsAndHashCode.Include @NonNull private String name;

        /**
         * The base64 value of this property.
         */
        @NonNull private String value;

        /**
         * The base64 signature of this property.
         */
        private String signature;

        /**
         * Is this property signed?
         *
         * @return whether this property has a signature
         */
        public boolean isSigned() {
            return signature != null;
        }
    }
}