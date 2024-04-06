package me.braydon.mc.model.token;

import com.google.gson.JsonObject;
import lombok.*;
import me.braydon.mc.RESTfulMC;
import me.braydon.mc.common.Tuple;
import me.braydon.mc.model.Cape;
import me.braydon.mc.model.ProfileAction;
import me.braydon.mc.model.Skin;

import java.util.Base64;

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

    public Tuple<Skin, Cape> getSkinAndCape() {
        ProfileProperty textures = getPropertyByName("textures"); // Get the profile textures
        if (textures == null) { // No profile textures
            return new Tuple<>();
        }
        JsonObject jsonObject = RESTfulMC.GSON.fromJson(textures.getDecodedValue(), JsonObject.class); // Get the Json object
        JsonObject texturesJsonObject = jsonObject.getAsJsonObject("textures"); // Get the textures object

        // Return the tuple containing the skin and cape
        return new Tuple<>(
                Skin.fromJsonObject(texturesJsonObject.getAsJsonObject("SKIN")),
                Cape.fromJsonObject(texturesJsonObject.getAsJsonObject("CAPE"))
        );
    }

    /**
     * Get the profile property
     * with the given name.
     *
     * @param name the property name
     * @return the profile property, null if none
     */
    public ProfileProperty getPropertyByName(@NonNull String name) {
        for (ProfileProperty property : properties) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }
        return null;
    }

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
         * Get the decoded Base64
         * value of this property.
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
}