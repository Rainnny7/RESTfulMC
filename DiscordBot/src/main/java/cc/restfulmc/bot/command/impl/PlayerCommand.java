package cc.restfulmc.bot.command.impl;

import cc.restfulmc.bot.command.SlashCommand;
import cc.restfulmc.sdk.client.RESTfulMCClient;
import cc.restfulmc.sdk.exception.RESTfulMCAPIException;
import cc.restfulmc.sdk.response.Player;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Braydon
 */
public final class PlayerCommand extends SlashCommand {
    /**
     * The API client to use for lookups.
     */
    @NonNull private final RESTfulMCClient apiClient;

    public PlayerCommand(@NonNull RESTfulMCClient apiClient) {
        super("player", "Lookup a player by their username or UUID",
                new OptionData(OptionType.STRING, "query", "The player username or UUID").setRequired(true)
        );
        this.apiClient = apiClient;
    }

    /**
     * Invoked when this command is executed.
     *
     * @param user   the executing user
     * @param member the executing member, null if in dms
     * @param event  the event that triggered this command
     */
    @Override
    public void onExecute(@NonNull User user, Member member, @NonNull SlashCommandInteractionEvent event) {
        OptionMapping query = event.getOption("query"); // Get the query
        assert query != null;
        String queryValue = query.getAsString(); // Get the query value

        // Lookup the requested player by the given query
        apiClient.async().getPlayer(queryValue).whenComplete((player, ex) -> {
            // Failed to lookup the player, handle the error
            if (ex != null) {
                if (ex.getCause() instanceof RESTfulMCAPIException apiError) {
                    replyWithApiError(event, apiError);
                } else { // Only print real errors
                    ex.printStackTrace();
                }
                return;
            }
            // Respond with the player
            long cached = player.getCached(); // The timestamp the player was cached
            event.getHook().sendMessageEmbeds(new EmbedBuilder()
                    .setColor(0x55FF55)
                    .setTitle("<:grass_block:1232798337300828181> Player Response", "https://api.restfulmc.cc/player/" + queryValue)
                    .addField("Unique ID", player.getUniqueId().toString(), true)
                    .addField("Username", player.getUsername(), true)
                    .addField("Legacy", player.isLegacy() ? "Yes" : "No", true)
                    .addField("Cached", cached == -1L ? "No" : "Yes, <t:" + (cached / 1000L) + ":R>", true)
                    .setThumbnail(player.getSkin().getParts().get(Player.SkinPart.HEAD))
                    .setFooter("Requested by " + user.getName() + " | " + user.getId(), user.getEffectiveAvatarUrl())
                    .build()).queue();
        });
    }
}