package me.braydon.mc.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the app.
 *
 * @author Braydon
 */
@Configuration @Getter
public class AppConfig {
    public static AppConfig INSTANCE;

    @Value("${server.publicUrl}")
    private String serverPublicUrl;

    @PostConstruct
    public void onInitialize() {
        INSTANCE = this;
    }
}