package cc.restfulmc.api.repository;

import cc.restfulmc.api.model.cache.CachedSkinPartTexture;
import cc.restfulmc.api.model.skin.ISkinPart;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for skin texture parts.
 *
 * @author Braydon
 * @see ISkinPart for skin parts
 */
public interface SkinPartTextureCacheRepository extends CrudRepository<CachedSkinPartTexture, String> { }