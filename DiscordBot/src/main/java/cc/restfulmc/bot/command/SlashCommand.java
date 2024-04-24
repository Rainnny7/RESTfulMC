package cc.restfulmc.bot.command;

import cc.restfulmc.sdk.exception.RESTfulMCAPIException;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * A wrapper for slash commands.
 *
 * @author Braydon
 */
@Getter
public abstract class SlashCommand {
    /**
     * The name of this command.
     */
    @NonNull private final String name;

    /**
     * The description of this command.
     */
    @NonNull private final String description;

    /**
     * Optional options for this command.
     */
    private final OptionData[] options;

    public SlashCommand(@NonNull String name, @NonNull String description, OptionData... options) {
        this.name = name;
        this.description = description;
        this.options = options;
    }

    /**
     * Invoked when this command is executed.
     *
     * @param user the executing user
     * @param member the executing member, null if in dms
     * @param event the event that triggered this command
     */
    public abstract void onExecute(@NonNull User user, Member member, @NonNull SlashCommandInteractionEvent event);

    /**
     * Reply to an interaction with an API error.
     *
     * @param event the event to reply to
     * @param apiError the api error to reply with
     */
    protected final void replyWithApiError(@NonNull SlashCommandInteractionEvent event, @NonNull RESTfulMCAPIException apiError) {
        event.getHook().sendMessageEmbeds(new EmbedBuilder()
                .setColor(0xAA0000)
                .setTitle(apiError.getCode() + " | API Error")
                .setDescription(apiError.getMessage())
                .build()).queue();
    }
}