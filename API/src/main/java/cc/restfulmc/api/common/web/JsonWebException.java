package cc.restfulmc.api.common.web;

import lombok.Getter;
import lombok.NonNull;

/**
 * This exception is raised when a
 * {@link JsonWebRequest} encounters an error.
 *
 * @author Braydon
 */
@Getter
public class JsonWebException extends RuntimeException {
    /**
     * The status code of the response.
     */
    private final int statusCode;

    protected JsonWebException(int statusCode, @NonNull String message) {
        super(message);
        this.statusCode = statusCode;
    }

    protected JsonWebException(int statusCode, @NonNull Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }
}