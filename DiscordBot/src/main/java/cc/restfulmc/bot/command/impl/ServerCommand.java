package cc.restfulmc.bot.command.impl;

import cc.restfulmc.bot.command.SlashCommand;
import cc.restfulmc.bot.common.StringUtils;
import cc.restfulmc.sdk.client.RESTfulMCClient;
import cc.restfulmc.sdk.exception.RESTfulMCAPIException;
import cc.restfulmc.sdk.response.server.BedrockMinecraftServer;
import cc.restfulmc.sdk.response.server.JavaMinecraftServer;
import cc.restfulmc.sdk.response.server.MinecraftServer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Braydon
 */
@Slf4j(topic = "Server Lookup Command")
public final class ServerCommand extends SlashCommand {
    private static final Map<MinecraftServer.Platform, String> MAPPED_PLATFORM_EMOJIS = new HashMap<>() {{
        put(MinecraftServer.Platform.JAVA, "<:grass_block:1232798337300828181>");
        put(MinecraftServer.Platform.BEDROCK, "<:bedrock_block:1232798365427830928>");
    }};
    private static final DecimalFormat PLAYERS_FORMAT = new DecimalFormat("#,##0");

    /**
     * The API client to use for lookups.
     */
    @NonNull private final RESTfulMCClient apiClient;

    public ServerCommand(@NonNull RESTfulMCClient apiClient) {
        super("server", "Lookup a server by its platform and hostname",
                new OptionData(OptionType.STRING, "platform", "The platform: JAVA | BEDROCK").setRequired(true),
                new OptionData(OptionType.STRING, "hostname", "The hostname of the server").setRequired(true)
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
        OptionMapping platformOption = event.getOption("platform"); // The platform to query
        assert platformOption != null;
        String platformValue = platformOption.getAsString();
        MinecraftServer.Platform platform; // The platform to lookup
        try {
            platform = MinecraftServer.Platform.valueOf(platformValue.toUpperCase());
        } catch (IllegalArgumentException ex) {
            replyWithGenericError(event, "Invalid Platform", "You must specify either **Java** or **Bedrock** as the platform.");
            return;
        }

        OptionMapping hostname = event.getOption("hostname"); // Get the hostname to query
        assert hostname != null;
        String hostnameValue = hostname.getAsString(); // Get the hostname value

        // Lookup the requested server by the given platform and hostname
        apiClient.async().getMinecraftServer(platform, hostnameValue).whenComplete((server, ex) -> {
            // Failed to lookup the server, handle the error
            if (ex != null) {
                if (ex.getCause() instanceof RESTfulMCAPIException apiError) {
                    replyWithApiError(event, apiError);
                } else { // Only print real errors
                    log.error("Failed fetching Minecraft Server:", ex);
                }
                return;
            }
            // Respond with the server
            String ip = server.getIp(); // The resolved IP of the server
            MinecraftServer.GeoLocation geo = server.getGeo(); // The geo location of the server
            MinecraftServer.Players players = server.getPlayers(); // The players on the server
            long cached = server.getCached(); // The timestamp the server was cached

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(0x55FF55)
                    .setTitle(
                            MAPPED_PLATFORM_EMOJIS.get(platform) + " " + StringUtils.capitalize(platform.name()) + " Server Response: " + server.getHostname(),
                            "https://api.restfulmc.cc/server/" + platformValue + "/" + hostnameValue
                    ).addField("IP", (ip == null ? server.getHostname() : ip) + ":" + server.getPort(), true);

            // Show Geo location if resolved
            if (geo != null) {
                MinecraftServer.GeoLocation.LocationData country = geo.getCountry(); // The server's country
                embed.addField("Located", ":flag_" + country.getCode().toLowerCase() + ": " + country.getName(), true);
            }

            // Append platform specific details to the embed
            if (server instanceof JavaMinecraftServer javaServer) {
                appendJavaEmbed(embed, javaServer);
            } else {
                appendBedrockEmbed(embed, (BedrockMinecraftServer) server);
            }

            event.getHook().sendMessageEmbeds(embed
                    .addField("Players", PLAYERS_FORMAT.format(players.getOnline()) + "/" + PLAYERS_FORMAT.format(players.getMax()), true)
                    .addField("Cached", cached == -1L ? "No" : "Yes, <t:" + (cached / 1000L) + ":R>", true)
                    .setThumbnail(server instanceof JavaMinecraftServer javaServer ? javaServer.getFavicon().getUrl() : "https://api.restfulmc.cc/server/icon/invalid")
                    .setFooter("Requested by " + user.getName() + " | " + user.getId(), user.getEffectiveAvatarUrl())
                    .build()).queue();
        });
    }

    /**
     * Append Java specific details to an embed.
     *
     * @param embed the embed to append to
     * @param javaServer the java server
     */
    private void appendJavaEmbed(@NonNull EmbedBuilder embed, @NonNull JavaMinecraftServer javaServer) {
        JavaMinecraftServer.Version version = javaServer.getVersion(); // The version of the server
        String protocolName = version.getProtocolName(); // The name of the server's version protocol
        String platform = version.getPlatform(); // The server's platform
        String world = javaServer.getWorld(); // The server's main world
        JavaMinecraftServer.Plugin[] plugins = javaServer.getPlugins(); // The server's plugins

        embed.addField("Version", version.getProtocol() + (protocolName == null ? "" : " (" + protocolName + ")"), true);
        if (platform != null) {
            embed.addField("Platform", platform, true);
        }
        if (world != null) {
            embed.addField("World", world, true);
        }
        if (plugins != null) {
            embed.addField("Plugins", String.valueOf(plugins.length), true);
        }

        embed.addField("Query", javaServer.isQueryEnabled() ? "Enabled" : "Disabled", true);
        embed.addField("Mojang Banned", javaServer.isMojangBanned() ? "Yes" : "No", true);
    }

    /**
     * Append Bedrock specific details to an embed.
     *
     * @param embed the embed to append to
     * @param bedrockServer the bedrock server
     */
    private void appendBedrockEmbed(@NonNull EmbedBuilder embed, @NonNull BedrockMinecraftServer bedrockServer) {
        BedrockMinecraftServer.Version version = bedrockServer.getVersion(); // The version of the server
        BedrockMinecraftServer.GameMode gamemode = bedrockServer.getGamemode(); // The game mode of the server

        embed.addField("Version", version.getProtocol() + " (" + version.getName() + ")", true);
        embed.addField("Edition", bedrockServer.getEdition().name(), true);
        embed.addField("GameMode", gamemode.getName() + " (" + gamemode.getNumericId() + ")", true);
    }
}