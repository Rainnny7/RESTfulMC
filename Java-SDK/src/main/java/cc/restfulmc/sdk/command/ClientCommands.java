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
package cc.restfulmc.sdk.command;

import cc.restfulmc.sdk.client.ClientConfig;
import cc.restfulmc.sdk.exception.RESTfulMCAPIException;
import cc.restfulmc.sdk.request.APIWebRequest;
import cc.restfulmc.sdk.response.Player;
import cc.restfulmc.sdk.response.server.MinecraftServer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * An executor to make
 * requests to the API with.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter(AccessLevel.PROTECTED)
public abstract class ClientCommands {
    /**
     * The config to make requests with.
     */
    @NonNull private final ClientConfig config;

    /**
     * Get a player by their username or UUID.
     *
     * @param query the player uuid or username
     * @return the found player
     * @throws RESTfulMCAPIException if an api error occurs
     */
    @NonNull
    protected final Player sendGetPlayerRequest(@NonNull String query) throws RESTfulMCAPIException {
        return APIWebRequest.builder()
                .endpoint(config.getApiEndpoint() + "/player/" + query)
                .build().execute(Player.class);
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
    @NonNull @SuppressWarnings("unchecked")
    protected final <T extends MinecraftServer> T sendGetMinecraftServerRequest(@NonNull MinecraftServer.Platform platform,
                                                                                @NonNull String hostname) throws RESTfulMCAPIException {
        return (T) APIWebRequest.builder()
                .endpoint(config.getApiEndpoint() + "/server/" + platform.name() + "/" + hostname)
                .build().execute(platform.getServerClass());
    }
}