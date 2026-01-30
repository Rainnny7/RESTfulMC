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