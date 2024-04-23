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
package cc.restfulmc.sdk.request;

import cc.restfulmc.sdk.client.RESTfulMCClient;
import cc.restfulmc.sdk.exception.RestfulMCAPIException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Braydon
 */
@Builder @Getter
public final class APIWebRequest {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    /**
     * The endpoint to make the request to.
     */
    @NonNull private final String endpoint;

    @SneakyThrows @NonNull
    public <T> T execute(@NonNull Class<T> responseType) {
        Request request = new Request.Builder()
                .url(endpoint)
                .build(); // Build the request
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            int status = response.code(); // The response status code
            String json = response.body().string(); // The json response
            if (status != 200) { // Not 200 (OK), throw an exception
                throw new RestfulMCAPIException(json);
            }
            return RESTfulMCClient.GSON.fromJson(json, responseType); // Return the response
        }
    }
}