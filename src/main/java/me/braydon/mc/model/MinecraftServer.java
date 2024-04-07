package me.braydon.mc.model;

import lombok.*;
import me.braydon.mc.service.pinger.MinecraftServerPinger;
import me.braydon.mc.service.pinger.impl.BedrockMinecraftServerPinger;
import me.braydon.mc.service.pinger.impl.JavaMinecraftServerPinger;

/**
 * A model representing a Minecraft server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class MinecraftServer {
    /**
     * The hostname of this server.
     */
    @EqualsAndHashCode.Include @NonNull private final String hostname;

    /**
     * The IP address of this server, if resolved.
     */
    private final String ip;

    /**
     * The port of this server.
     */
    @EqualsAndHashCode.Include private final int port;

    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The player counts of this server.
     */
    @NonNull private final Players players;

    /**
     * Version information for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Version {
        /**
         * The version name of the server.
         */
        @NonNull private final String name;

        /**
         * The protocol version of the server.
         */
        private final int protocol;
    }

    /**
     * Player count data for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Players {
        /**
         * The online players on this server.
         */
        private final int online;

        /**
         * The maximum allowed players on this server.
         */
        private final int max;
    }

    /**
     * A platform a Minecraft
     * server can operate on.
     */
    @AllArgsConstructor @Getter
    public enum Platform {
        /**
         * The Java edition of Minecraft.
         */
        JAVA(new JavaMinecraftServerPinger()),

        /**
         * The Bedrock edition of Minecraft.
         */
        BEDROCK(new BedrockMinecraftServerPinger());

        /**
         * The server pinger for this platform.
         */
        @NonNull private final MinecraftServerPinger<?> pinger;
    }
}