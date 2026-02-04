package cc.restfulmc.api.repository;

import cc.restfulmc.api.model.server.MinecraftServer;
import cc.restfulmc.api.model.server.cache.CachedMinecraftServerMOTDTexture;
import org.springframework.data.repository.CrudRepository;

/**
 * A cache repository for {@link MinecraftServer} MOTD.
 *
 * @author Braydon
 */
public interface MinecraftServerMOTDTextureCacheRepository extends CrudRepository<CachedMinecraftServerMOTDTexture, String> { }