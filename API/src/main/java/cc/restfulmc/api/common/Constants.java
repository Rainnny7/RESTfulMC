package cc.restfulmc.api.common;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * @author Braydon
 */
public final class Constants {
    public static final Duration HTTP_CLIENT_TIMEOUT = Duration.ofMillis(5000L);
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(HTTP_CLIENT_TIMEOUT)
            .build();
}