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
package cc.restfulmc.api.model;

import cc.restfulmc.api.model.skin.Skin;
import cc.restfulmc.api.model.token.MojangProfileToken;
import lombok.*;

import java.util.UUID;

/**
 * A model representing a player.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class Player {
    /**
     * The unique id of this player.
     */
    @EqualsAndHashCode.Include @NonNull private final UUID uniqueId;

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
    @NonNull private final MojangProfileToken.ProfileProperty[] properties;

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
}