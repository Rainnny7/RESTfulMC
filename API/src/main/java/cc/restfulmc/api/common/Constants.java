package cc.restfulmc.api.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Braydon
 */
public final class Constants {
    public static final Duration HTTP_CLIENT_TIMEOUT = Duration.ofMillis(5000L);
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(HTTP_CLIENT_TIMEOUT)
            .build();
    public static final Gson GSON = new GsonBuilder()
            .setDateFormat("MM-dd-yyyy HH:mm:ss")
            .create();
    public static final String REQUEST_START_TIME_ATTRIBUTE = "requestStartTime";
    public static final ExecutorService VIRTUAL_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
}