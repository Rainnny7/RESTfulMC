package me.braydon.mc.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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
@RedisHash(value = "player", timeToLive = 60L * 60L) // 1 hour (in seconds)
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
     * The profile actions this player has, null if none.
     */
    private ProfileAction[] profileActions;
}