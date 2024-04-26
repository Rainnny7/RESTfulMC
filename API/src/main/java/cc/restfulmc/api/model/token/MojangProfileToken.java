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
package cc.restfulmc.api.model.token;

import cc.restfulmc.api.config.AppConfig;
import cc.restfulmc.api.model.Cape;
import cc.restfulmc.api.model.ProfileAction;
import cc.restfulmc.api.model.skin.Skin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import lombok.*;

import java.util.Base64;

/**
 * A token representing a Mojang user profile.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Mojang_API#UUID_to_Profile_and_Skin.2FCape">Mojang API</a>
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public final class MojangProfileToken {
    /**
     * The id of the profile.
     */
    @EqualsAndHashCode.Include @NonNull private final String id;

    /**
     * The name of the profile.
     */
    @NonNull private final String name;

    /**
     * The properties of the profile.
     */
    @NonNull private final ProfileProperty[] properties;

    /**
     * The actions this profile has.
     */
    @NonNull private final ProfileAction[] profileActions;

    /**
     * Is this profile legacy?
     * <p>
     * A "Legacy" profile is a profile that
     * has not yet migrated to a Mojang account.
     * </p>
     */
    private final boolean legacy;

    /**
     * Get the properties of this skin.
     *
     * @return the properties
     */
    @NonNull
    public SkinProperties getSkinProperties() {
        ProfileProperty textures = getPropertyByName("textures"); // Get the profile textures
        if (textures == null) { // No profile textures
            return new SkinProperties();
        }
        JsonObject jsonObject = AppConfig.GSON.fromJson(textures.getDecodedValue(), JsonObject.class); // Get the Json object
        JsonObject texturesJsonObject = jsonObject.getAsJsonObject("textures"); // Get the textures object

        // Return the tuple containing the skin and cape
        return new SkinProperties(
                Skin.fromJsonObject(texturesJsonObject.getAsJsonObject("SKIN")).populatePartUrls(id),
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
        @NonNull @JsonIgnore
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
     * The properties for a skin.
     */
    @NoArgsConstructor @AllArgsConstructor @Getter @ToString
    public static class SkinProperties {
        /**
         * The skin of the profile.
         */
        private Skin skin;

        /**
         * The cape of the profile.
         */
        private Cape cape;
    }
}