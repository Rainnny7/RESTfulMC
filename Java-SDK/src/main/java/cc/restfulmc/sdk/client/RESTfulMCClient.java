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

import cc.restfulmc.sdk.command.impl.AsyncClientCommands;
import cc.restfulmc.sdk.command.impl.SyncClientCommands;
import cc.restfulmc.sdk.response.server.dns.DNSRecord;
import cc.restfulmc.sdk.serializer.DNSRecordSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * @author Braydon
 */
@Getter @Accessors(fluent = true)
public final class RESTfulMCClient {
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(DNSRecord.class, new DNSRecordSerializer())
            .create();

    /**
     * The config for this client.
     */
    @NonNull @Getter(AccessLevel.NONE) private final ClientConfig config;

    /**
     * Synchronized commands for this client.
     */
    @NonNull private final SyncClientCommands sync;

    /**
     * Asynchronous commands for this client.
     */
    @NonNull private final AsyncClientCommands async;

    public RESTfulMCClient(@NonNull ClientConfig config) {
        this.config = config;
        sync = new SyncClientCommands(config);
        async = new AsyncClientCommands(config);
    }
}