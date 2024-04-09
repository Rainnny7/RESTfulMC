/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny), and the RESTfulMC contributors
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
     * Get the skin and cape of this profile.
     *
     * @return the skin and cape of this profile
     */
    public Tuple<Skin, Cape> getSkinAndCape() {
        ProfileProperty textures = getPropertyByName("textures"); // Get the profile textures
        if (textures == null) { // No profile textures
            return new Tuple<>();
        }
        JsonObject jsonObject = RESTfulMC.GSON.fromJson(textures.getDecodedValue(), JsonObject.class); // Get the Json object
        JsonObject texturesJsonObject = jsonObject.getAsJsonObject("textures"); // Get the textures object

        // Return the tuple containing the skin and cape
        return new Tuple<>(
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