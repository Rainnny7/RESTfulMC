package me.braydon.mc.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

/**
 * A model representing a player.
 *
 * @author Braydon
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Player {
    /**
     * The unique id of this player.
     */
    @Id @EqualsAndHashCode.Include @NonNull private UUID uniqueId;

    /**
     * The username of this player.
     */
    @NonNull private String username;

    /**
     * The skin of this player.
     */
    @NonNull private Skin skin;

    /**
     * The cape of this player, null if none.
     */
    private Cape cape;

    /**
     * The profile actions this player has, null if none.
     */
    private ProfileAction[] profileActions;
}