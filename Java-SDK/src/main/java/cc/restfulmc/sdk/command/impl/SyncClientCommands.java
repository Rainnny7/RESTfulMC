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
package cc.restfulmc.sdk.command.impl;

import cc.restfulmc.sdk.client.ClientConfig;
import cc.restfulmc.sdk.client.RESTfulMCClient;
import cc.restfulmc.sdk.command.ClientCommands;
import cc.restfulmc.sdk.exception.RESTfulMCAPIException;
import cc.restfulmc.sdk.response.MojangServerStatus;
import cc.restfulmc.sdk.response.Player;
import cc.restfulmc.sdk.response.server.MinecraftServer;
import lombok.NonNull;

/**
 * Synchronized commands for the {@link RESTfulMCClient}.
 *
 * @author Braydon
 */
public final class SyncClientCommands extends ClientCommands {
    public SyncClientCommands(@NonNull ClientConfig config) {
        super(config);
    }

    /**
     * Get a player by their username or UUID.
     *
     * @param query the player uuid or username
     * @return the found player
     * @throws RESTfulMCAPIException if an api error occurs
     */
    @NonNull
    public Player getPlayer(@NonNull String query) throws RESTfulMCAPIException {
        return sendGetPlayerRequest(query);
    }

    /**
     * Get a Minecraft server by its platform and hostname.
     *
     * @param platform the platform of the server
     * @param hostname the hostname of the server
     * @return the server
     * @param <T> the server type
     * @throws RESTfulMCAPIException if an api error occurs
     */
    @NonNull
    public <T extends MinecraftServer> T getMinecraftServer(@NonNull MinecraftServer.Platform platform, @NonNull String hostname) throws RESTfulMCAPIException {
        return sendGetMinecraftServerRequest(platform, hostname);
    }

    /**
     * Check if the server with the
     * given hostname is blocked by Mojang.
     *
     * @param hostname the hostname of the server
     * @return whether the server is blocked
     * @throws RESTfulMCAPIException if an api error occurs
     */
    public boolean isMojangBlocked(@NonNull String hostname) throws RESTfulMCAPIException {
        return sendIsServerBlockedRequest(hostname);
    }

    /**
     * Get the status of Mojang servers.
     *
     * @return the status of Mojang servers
     * @throws RESTfulMCAPIException if an api error occurs
     */
    public MojangServerStatus getMojangStatus() throws RESTfulMCAPIException {
        return sendGetMojangStatusRequest();
    }
}