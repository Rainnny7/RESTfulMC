package cc.restfulmc.api.model.server.cache;

import cc.restfulmc.api.model.server.MinecraftServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * A cache for a {@link MinecraftServer} MOTD texture.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
@RedisHash(value = "serverMotd", timeToLive = 5L * 60L) // 5 minutes (in seconds)
public final class CachedMinecraftServerMOTDTexture implements Serializable {
    /**
     * The id of this cache element.
     * <p>
     * This ID is in the given format:
     * serverMotd:<platform>-<hostname>-<size>-<ext>
     * </p>
     */
    @Id private transient final String id;

    /**
     * The cached texture.
     */
    private final byte[] texture;
}