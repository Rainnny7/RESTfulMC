package cc.restfulmc.api.model.cache;

import cc.restfulmc.api.model.MinecraftServer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author Braydon
 */
@AllArgsConstructor @Setter @Getter @ToString
@RedisHash(value = "server", timeToLive = 60L) // 1 minute (in seconds)
public final class CachedMinecraftServer implements Serializable {
    /**
     * The id of this cache element.
     */
    @Id @JsonIgnore @NonNull private final String id;

    /**
     * The cached server.
     */
    @JsonUnwrapped @NonNull private final MinecraftServer value;

    /**
     * The unix timestamp of when this
     * server was cached, -1 if not cached.
     */
    private long cached;
}