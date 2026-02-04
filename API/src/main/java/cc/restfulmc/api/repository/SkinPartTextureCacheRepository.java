package cc.restfulmc.api.repository;

import cc.restfulmc.api.model.player.cache.CachedSkinPartTexture;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for skin texture parts.
 *
 * @author Braydon
 */
public interface SkinPartTextureCacheRepository extends CrudRepository<CachedSkinPartTexture, String> { }