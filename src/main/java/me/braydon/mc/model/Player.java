package me.braydon.mc.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

/**
 * A model representing a player.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Player {
    /**
     * The unique id of this player.
     */
    @Id @EqualsAndHashCode.Include @NonNull private final UUID uniqueId;

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
     * The profile actions this player has, null if none.
     */
    private final ProfileAction[] profileActions;
}