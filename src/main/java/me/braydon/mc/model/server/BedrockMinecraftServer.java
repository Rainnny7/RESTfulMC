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
package me.braydon.mc.model.server;

import lombok.*;
import me.braydon.mc.model.MinecraftServer;

/**
 * A Bedrock edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@Getter @ToString(callSuper = true) @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public final class BedrockMinecraftServer extends MinecraftServer {
    /**
     * The unique ID of this server.
     */
    @EqualsAndHashCode.Include @NonNull private final String uniqueId;

    /**
     * The edition of this server.
     */
    @NonNull private final Edition edition;

    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The gamemode of this server.
     */
    @NonNull private final GameMode gamemode;

    private BedrockMinecraftServer(@NonNull String uniqueId, @NonNull String hostname, String ip, int port,
                                   @NonNull Edition edition, @NonNull Version version, @NonNull Players players,
                                   @NonNull MOTD motd, @NonNull GameMode gamemode) {
        super(hostname, ip, port, players, motd);
        this.uniqueId = uniqueId;
        this.edition = edition;
        this.version = version;
        this.gamemode = gamemode;
    }

    /**
     * Create a new Bedrock Minecraft server.
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param token the status token
     * @return the Bedrock Minecraft server
     */
    @NonNull
    public static BedrockMinecraftServer create(@NonNull String hostname, String ip, int port, @NonNull String token) {
        String[] split = token.split(";"); // Split the token
        Edition edition = Edition.valueOf(split[0]);
        Version version = new Version(Integer.parseInt(split[2]), split[3]);
        Players players = new Players(Integer.parseInt(split[4]), Integer.parseInt(split[5]), null);
        MOTD motd = MOTD.create(split[1] + "\n" + split[7]);
        GameMode gameMode = new GameMode(split[8], Integer.parseInt(split[9]));
        return new BedrockMinecraftServer(split[6], hostname, ip, port, edition, version, players, motd, gameMode);
    }

    /**
     * The edition of a Bedrock server.
     */
    @AllArgsConstructor @Getter
    public enum Edition {
        /**
         * Minecraft: Pocket Edition.
         */
        MCPE,

        /**
         * Minecraft: Education Edition.
         */
        MCEE
    }

    /**
     * Version information for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Version {
        /**
         * The protocol version of the server.
         */
        private final int protocol;

        /**
         * The version name of the server.
         */
        @NonNull private final String name;
    }

    /**
     * The gamemode of a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class GameMode {
        /**
         * The name of this gamemode.
         */
        @NonNull private final String name;

        /**
         * The numeric of this gamemode.
         */
        private final int numericId;
    }
}