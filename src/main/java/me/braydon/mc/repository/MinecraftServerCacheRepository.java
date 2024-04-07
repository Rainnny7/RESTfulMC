package me.braydon.mc.repository;

import me.braydon.mc.model.cache.CachedMinecraftServer;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for {@link CachedMinecraftServer}'s.
 *
 * @author Braydon
 */
public interface MinecraftServerCacheRepository extends CrudRepository<CachedMinecraftServer, String> { }