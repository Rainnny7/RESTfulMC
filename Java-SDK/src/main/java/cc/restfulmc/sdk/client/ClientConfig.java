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
package cc.restfulmc.sdk.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Configuration for a {@link RESTfulMCClient}.
 *
 * @author Braydon
 */
@Builder @Getter
public final class ClientConfig {
    /**
     * The API endpoint to make requests to.
     */
    @NonNull private final String apiEndpoint;

    /**
     * Should debugging be enabled?
     */
    private final boolean debugging;

    /**
     * Get the default client config.
     *
     * @return the default config
     */
    @NonNull
    public static ClientConfig defaultConfig() {
        return ClientConfig.builder()
                .apiEndpoint("https://api.restfulmc.cc")
                .build();
    }
}