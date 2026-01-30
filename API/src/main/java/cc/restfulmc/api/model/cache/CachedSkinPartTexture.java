package cc.restfulmc.api.model.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * A cache for a skin part texture.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
@RedisHash(value = "skinPart", timeToLive = 15L * 60L) // 15 minutes (in seconds)
public final class CachedSkinPartTexture implements Serializable {
    /**
     * The id of this cache element.
     * <p>
     * This ID is in the given format:
     * skinPart:<query>-<part>-<renderOverlays>-<size>-<ext>
     * </p>
     */
    @Id private transient final String id;

    /**
     * The cached texture;
     */
    private final byte[] texture;
}