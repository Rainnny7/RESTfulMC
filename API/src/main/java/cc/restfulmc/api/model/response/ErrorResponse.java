package cc.restfulmc.api.model.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * A response representing an error.
 *
 * @author Braydon
 */
@Getter @ToString
public final class ErrorResponse {
    /**
     * The status code of this error.
     */
    @NonNull private final HttpStatus status;

    /**
     * The HTTP code of this error.
     */
    private final int code;

    /**
     * The message of this error.
     */
    @NonNull private final String message;

    /**
     * The timestamp this error occurred.
     */
    @NonNull private final Date timestamp;

    public ErrorResponse(@NonNull HttpStatus status, @NonNull String message) {
        this.status = status;
        code = status.value();
        this.message = message;
        timestamp = new Date();
    }
}