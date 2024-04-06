package me.braydon.mc.common.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.braydon.mc.RESTfulMC;
import org.springframework.http.HttpMethod;

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
            if (status != 200) { // Status code is not OK, raise an exception
                throw new IOException("Failed to make a %s request to %s: %s".formatted(method.name(), endpoint, status));
            }
            // Return with the response as the type
            return RESTfulMC.GSON.fromJson(response.body(), responseType);
        } catch (Exception ex) {
            if (!(ex instanceof JsonWebException)) {
                throw new JsonWebException(status, ex);
            }
            throw (JsonWebException) ex;
        }
    }
}