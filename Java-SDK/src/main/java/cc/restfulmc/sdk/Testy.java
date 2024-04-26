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
package cc.restfulmc.sdk;

import cc.restfulmc.sdk.client.ClientConfig;
import cc.restfulmc.sdk.client.RESTfulMCClient;
import cc.restfulmc.sdk.command.impl.SyncClientCommands;
import cc.restfulmc.sdk.exception.RESTfulMCAPIException;
import cc.restfulmc.sdk.response.MojangServerStatus;
import cc.restfulmc.sdk.response.Player;
import cc.restfulmc.sdk.response.server.BedrockMinecraftServer;
import cc.restfulmc.sdk.response.server.JavaMinecraftServer;
import cc.restfulmc.sdk.response.server.MinecraftServer;
import cc.restfulmc.sdk.response.server.dns.DNSRecord;
import lombok.SneakyThrows;

/**
 * @author Braydon
 */
public final class Testy {
    @SneakyThrows
    public static void main(String[] args) {
        RESTfulMCClient client = new RESTfulMCClient(ClientConfig.defaultConfig()); // Create the client
        SyncClientCommands sync = client.sync(); // Get synchronous commands

        // Get a player
        try {
            Player player = sync.getPlayer("rAINnny");
            System.out.printf("%s's UUID is %s%n", player.getUsername(), player.getUniqueId());
        } catch (RESTfulMCAPIException ex) {
            if (ex.getCode() == 400 || ex.getCode() == 404) {
                System.out.println("Player not found!");
            }
        }

        // Get a Java Minecraft server
        try {
            JavaMinecraftServer server = sync.getMinecraftServer(MinecraftServer.Platform.JAVA, "hypixel.net");
            MinecraftServer.Players players = server.getPlayers();
            System.out.printf("%s has %s/%s players online%n", server.getHostname(), players.getOnline(), players.getMax());

            for (DNSRecord record : server.getRecords()) {
                System.out.println("record = " + record);
            }
        } catch (RESTfulMCAPIException ex) {
            if (ex.getCode() == 400 || ex.getCode() == 404) {
                System.out.println("Server not found!");
            }
        }

        // Get a Bedrock Minecraft server
        try {
            BedrockMinecraftServer server = sync.getMinecraftServer(MinecraftServer.Platform.BEDROCK, "wildprison.bedrock.minehut.gg");
            MinecraftServer.Players players = server.getPlayers();
            System.out.printf("%s has %s/%s players online%n", server.getHostname(), players.getOnline(), players.getMax());
        } catch (RESTfulMCAPIException ex) {
            if (ex.getCode() == 400 || ex.getCode() == 404) {
                System.out.println("Server not found!");
            }
        }

        System.out.println("mojang blocked? = " + sync.isMojangBlocked("arkhamnetwork.org"));

        MojangServerStatus status = sync.getMojangStatus();
        System.out.println("status = " + status.getServers()[0]);
    }
}