package me.braydon.mc.model.server;

import lombok.NonNull;
import me.braydon.mc.RESTfulMC;
import me.braydon.mc.model.MinecraftServer;
import me.braydon.mc.model.token.JavaServerStatusToken;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * A Java edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
public final class JavaMinecraftServer extends MinecraftServer {
    private JavaMinecraftServer(@NonNull String hostname, String ip, int port, @NonNull Version version,
                                @NonNull Players players, @NonNull MOTD motd, String icon, boolean mojangBanned) {
        super(hostname, ip, port, version, players, motd, icon, mojangBanned);
    }

    /**
     * Create a new Java Minecraft server.
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param token the status token
     * @return the Java Minecraft server
     */
    @NonNull
    public static JavaMinecraftServer create(@NonNull String hostname, String ip, int port, @NonNull JavaServerStatusToken token) {
        String motdString = token.getDescription() instanceof String ? (String) token.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = new TextComponent(ComponentSerializer.parse(RESTfulMC.GSON.toJson(token.getDescription()))).toLegacyText();
        }
        return new JavaMinecraftServer(hostname, ip, port, token.getVersion().detailedCopy(),
                token.getPlayers(), MOTD.create(motdString), token.getFavicon(), false
        );
    }
}