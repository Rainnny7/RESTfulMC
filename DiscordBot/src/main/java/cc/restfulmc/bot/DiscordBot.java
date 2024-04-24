package cc.restfulmc.bot;

import cc.restfulmc.bot.command.CommandManager;
import cc.restfulmc.sdk.client.ClientConfig;
import cc.restfulmc.sdk.client.RESTfulMCClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;

/**
 * @author Braydon
 */
@Slf4j(topic = "RESTfulMC Bot") @Getter
public final class DiscordBot {
    /**
     * The JDA bot instance.
     */
    private JDA jda;

    @SneakyThrows
    public DiscordBot() {
        String token = System.getenv("BOT_TOKEN");
        if (token == null) { // Missing BOT_TOKEN
            throw new NullPointerException("Missing BOT_TOKEN environment variable");
        }
        jda = JDABuilder.createLight(token)
                .setActivity(Activity.watching("Minecraft servers"))
                .build();
        jda.awaitReady(); // Wait for JDA to become ready

        // Setup the API SDK
        RESTfulMCClient apiClient = new RESTfulMCClient(ClientConfig.defaultConfig());

        // Commands
        new CommandManager(this, apiClient);

        SelfUser self = jda.getSelfUser();
        log.info("Logged in as bot {} ({})", self.getAsTag(), self.getId());

        // Add a cleanup hook to cleanup the bot when the JVM shuts down
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
    }

    /**
     * Cleanup the bot.
     */
    public void cleanup() {
        log.info("Cleaning up...");
        jda.shutdown();
        jda = null;
        log.info("Goodbye!");
    }

    public static void main(@NonNull String[] args) {
        new DiscordBot();
    }
}