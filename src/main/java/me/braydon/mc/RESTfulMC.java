package me.braydon.mc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author Braydon
 */
@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
@Slf4j(topic = "RESTfulMC")
public class RESTfulMC {
    public static final Gson GSON = new GsonBuilder()
            .setDateFormat("MM-dd-yyyy HH:mm:ss")
            .create();

    /**
     * The Redis server host.
     */
    @Value("${spring.data.redis.host}")
    private String redisHost;

    /**
     * The Redis server port.
     */
    @Value("${spring.data.redis.port}")
    private int redisPort;

    /**
     * The Redis database index.
     */
    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    /**
     * The optional Redis password.
     */
    @Value("${spring.data.redis.auth}")
    private String redisAuth;

    @SneakyThrows
    public static void main(@NonNull String[] args) {
        // Handle loading of our configuration file
        File config = new File("application.yml");
        if (!config.exists()) { // Saving the default config if it doesn't exist locally
            Files.copy(Objects.requireNonNull(RESTfulMC.class.getResourceAsStream("/application.yml")), config.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("Saved the default configuration to '{}', please re-launch the application", // Log the default config being saved
                    config.getAbsolutePath()
            );
            return;
        }
        log.info("Found configuration at '{}'", config.getAbsolutePath()); // Log the found config

        // Start the app
        SpringApplication.run(RESTfulMC.class, args);
    }

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
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(redisDatabase);
        if (!redisAuth.trim().isEmpty()) { // Auth with our provided password
            config.setPassword(redisAuth);
        }
        return new JedisConnectionFactory(config);
    }
}