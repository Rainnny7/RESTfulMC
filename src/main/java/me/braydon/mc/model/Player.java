package me.braydon.mc.model;

import lombok.*;

import java.util.UUID;

/**
 * A model representing a player.
 *
 * @author Braydon
 */
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public final class Player {
    /**
     * The unique id of this player.
     */
    @EqualsAndHashCode.Include @NonNull private UUID uniqueId;

    /**
     * The username of this player.
     */
    @NonNull private String username;

    /**
     * The profile actions this player has.
     */
    @NonNull private ProfileAction[] profileActions;
}