package cc.restfulmc.api.repository;

import cc.restfulmc.api.model.cache.CachedPlayerName;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for player usernames.
 * <p>
 * This will allow us to easily lookup a
 * player's username and get their uuid.
 * </p>
 *
 * @author Braydon
 */
public interface PlayerNameCacheRepository extends CrudRepository<CachedPlayerName, String> { }