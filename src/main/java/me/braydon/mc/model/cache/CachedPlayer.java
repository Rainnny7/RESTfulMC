package me.braydon.mc.model.cache;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import me.braydon.mc.model.Player;
import me.braydon.mc.model.ProfileAction;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

/**
 * A cacheable {@link Player}.
 *
 * @author Braydon
 */
@Setter @Getter
@ToString(callSuper = true)
@RedisHash(value = "player", timeToLive = 60L * 60L) // 1 hour (in seconds)
public final class CachedPlayer extends Player implements Serializable {
    /**
     * The unix timestamp of when this
     * player was cached, -1 if not cached.
     */
    private long cached;

    public CachedPlayer(@NonNull UUID uniqueId, @NonNull String username, ProfileAction[] profileActions, long cached) {
        super(uniqueId, username, profileActions);
        this.cached = cached;
    }
}