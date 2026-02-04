package cc.restfulmc.api.model.cache;

import cc.restfulmc.api.model.player.Cape;
import cc.restfulmc.api.model.player.Player;
import cc.restfulmc.api.model.player.ProfileAction;
import cc.restfulmc.api.model.player.skin.Skin;
import cc.restfulmc.api.model.token.mojang.MojangProfileToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
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
     * The id of this cache element.
     * <p>
     * This ID is in the given format:
     * player:<uniqueId>-<signed>
     * </p>
     */
    @Id @NonNull @JsonIgnore private final String cacheId;

    /**
     * The unix timestamp of when this
     * player was cached, -1 if not cached.
     */
    private long cached;

    public CachedPlayer(@NonNull String cacheId, @NonNull UUID uniqueId, @NonNull String username, @NonNull Skin skin,
                        Cape cape, @NonNull MojangProfileToken.ProfileProperty[] properties, ProfileAction[] profileActions,
                        boolean legacy, long cached) {
        super(uniqueId, username, skin, cape, properties, profileActions, legacy);
        this.cacheId = cacheId;
        this.cached = cached;
    }
}