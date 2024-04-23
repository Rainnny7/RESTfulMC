/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cc.restfulmc.api.model.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import cc.restfulmc.api.model.Cape;
import cc.restfulmc.api.model.Player;
import cc.restfulmc.api.model.ProfileAction;
import cc.restfulmc.api.model.skin.Skin;
import cc.restfulmc.api.model.token.MojangProfileToken;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

/**
 * A cacheable {@link Player}.
 *
 * @author Braydon
 */
@Setter @Getter
@ToString(callSuper = true)
@RedisHash(value = "player", timeToLive = 60L * 60L) // 1 hour (in seconds)
public final class CachedPlayer extends Player implements Serializable {
    /**
     * The id of this cache element.
     * <p>
     * This ID is in the given format:
     * player:<uniqueId>-<signed>
     * </p>
     */
    @Id @NonNull @JsonIgnore private final String cacheId;

    /**
     * The unix timestamp of when this
     * player was cached, -1 if not cached.
     */
    private long cached;

    public CachedPlayer(@NonNull String cacheId, @NonNull UUID uniqueId, @NonNull String username, @NonNull Skin skin,
                        Cape cape, @NonNull MojangProfileToken.ProfileProperty[] properties, ProfileAction[] profileActions,
                        boolean legacy, long cached) {
        super(uniqueId, username, skin, cape, properties, profileActions, legacy);
        this.cacheId = cacheId;
        this.cached = cached;
    }
}