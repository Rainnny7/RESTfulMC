package cc.restfulmc.bot.command;

import cc.restfulmc.bot.DiscordBot;
import cc.restfulmc.bot.command.impl.PlayerCommand;
import cc.restfulmc.bot.command.impl.ServerCommand;
import cc.restfulmc.sdk.client.RESTfulMCClient;
import lombok.NonNull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Braydon
 */
public final class CommandManager extends ListenerAdapter {
    /**
     * The registered slash commands.
     */
    private final List<SlashCommand> commands = Collections.synchronizedList(new ArrayList<>());

    public CommandManager(@NonNull DiscordBot bot, @NonNull RESTfulMCClient apiClient) {
        // Register commands
        registerCommand(new PlayerCommand(apiClient));
        registerCommand(new ServerCommand(apiClient));

        // Update the commands on Discord
        CommandListUpdateAction updateCommands = bot.getJda().updateCommands();
        for (SlashCommand command : commands) {
            updateCommands.addCommands(Commands.slash(command.getName(), command.getDescription()).addOptions(command.getOptions()));
        }
        updateCommands.queue();

        // Handle registered events
        bot.getJda().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(@NonNull SlashCommandInteractionEvent event) {
        for (SlashCommand command : commands) {
            if (command.getName().equals(event.getName())) {
                event.deferReply().queue(); // Inform Discord we received the command
                command.onExecute(event.getUser(), event.getMember(), event); // Invoke the command
                break;
            }
        }
    }

    /**
     * Register a slash command.
     *
     * @param command the command to register
     */
    public void registerCommand(@NonNull SlashCommand command) {
        commands.add(command);
    }
}