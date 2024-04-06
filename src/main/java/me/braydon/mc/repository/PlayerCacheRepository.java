package me.braydon.mc.repository;

import me.braydon.mc.model.Player;
import me.braydon.mc.model.cache.CachedPlayer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * A cache repository for {@link Player}'s.
 *
 * @author Braydon
 */
public interface PlayerCacheRepository extends CrudRepository<CachedPlayer, UUID> { }