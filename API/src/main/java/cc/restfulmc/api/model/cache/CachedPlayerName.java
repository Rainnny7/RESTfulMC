package cc.restfulmc.api.model.cache;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

/**
 * A cache to easily lookup a
 * player's UUID by their username.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
@RedisHash(value = "playerName", timeToLive = 60L * 60L) // 1 hour (in seconds)
public final class CachedPlayerName {
    /**
     * The username of the player.
     */
    @Id @NonNull private String username;

    /**
     * The unique id of the player.
     */
    @NonNull private UUID uniqueId;
}