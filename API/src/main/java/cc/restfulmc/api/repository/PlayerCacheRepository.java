package cc.restfulmc.api.repository;

import cc.restfulmc.api.model.player.cache.CachedPlayer;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for {@link CachedPlayer}'s.
 *
 * @author Braydon
 */
public interface PlayerCacheRepository extends CrudRepository<CachedPlayer, String> { }