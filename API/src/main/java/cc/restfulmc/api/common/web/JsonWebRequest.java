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
package cc.restfulmc.api.common.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import cc.restfulmc.api.config.AppConfig;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A Json web request.
 *
 * @author Braydon
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @Getter
public final class JsonWebRequest {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    /**
     * The endpoint to make the request to.
     */
    @NonNull private final String endpoint;

    /**
     * The method to use for the request.
     */
    @NonNull private final HttpMethod method;

    /**
     * The headers to send with the request.
     */
    private final Map<String, String> headers = Collections.synchronizedMap(new HashMap<>());

    /**
     * Make a new web request.
     *
     * @param endpoint the endpoint to make the request to
     * @param method the method of the request to make
     * @return the web request
     */
    @NonNull
    public static JsonWebRequest makeRequest(@NonNull String endpoint, @NonNull HttpMethod method) {
        return new JsonWebRequest(endpoint, method);
    }

    /**
     * Set a header for this request.
     *
     * @param name the header name
     * @param value the header value
     * @return the request
     */
    @NonNull
    public JsonWebRequest header(@NonNull String name, @NonNull String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Execute this request.
     *
     * @param responseType the response type
     * @return the response
     * @param <T> the type of the response
     * @throws JsonWebException if an error is encountered while making the request
     */
    public <T> T execute(@NonNull Class<T> responseType) throws JsonWebException {
        // Build the request
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .method(method.name(), HttpRequest.BodyPublishers.noBody());
        // Append headers
        headers.put("User-Agent", "RESTfulMC");
        headers.put("Content-Type", "application/json");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }

        // Send the request and get the response
        int status = -1; // The response status code
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request.build(), HttpResponse.BodyHandlers.ofString());
            status = response.statusCode(); // Set the response status
            if (status != HttpStatus.OK.value()) { // Status code is not OK, raise an exception
                throw new IOException("Failed to make a %s request to %s: %s".formatted(method.name(), endpoint, status));
            }
            // Return with the response as the type
            return AppConfig.GSON.fromJson(response.body(), responseType);
        } catch (Exception ex) {
            if (!(ex instanceof JsonWebException)) {
                throw new JsonWebException(status, ex);
            }
            throw (JsonWebException) ex;
        }
    }
}