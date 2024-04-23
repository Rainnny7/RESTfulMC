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
package cc.restfulmc.api.config;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Braydon
 */
@Configuration
@Log4j2(topic = "Redis")
public class RedisConfig {
    /**
     * The Redis server host.
     */
    @Value("${spring.data.redis.host}")
    private String host;

    /**
     * The Redis server port.
     */
    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * The Redis database index.
     */
    @Value("${spring.data.redis.database}")
    private int database;

    /**
     * The optional Redis password.
     */
    @Value("${spring.data.redis.auth}")
    private String auth;

    /**
     * Build the config to use for Redis.
     *
     * @return the config
     * @see RedisTemplate for config
     */
    @Bean @NonNull
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    /**
     * Build the connection factory to use
     * when making connections to Redis.
     *
     * @return the built factory
     * @see JedisConnectionFactory for factory
     */
    @Bean @NonNull
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Connecting to Redis at {}:{}/{}", host, port, database);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(database);
        if (!auth.trim().isEmpty()) { // Auth with our provided password
            log.info("Using auth...");
            config.setPassword(auth);
        }
        return new JedisConnectionFactory(config);
    }
}