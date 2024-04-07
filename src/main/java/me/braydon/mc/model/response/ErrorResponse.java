package me.braydon.mc.model.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * A response representing an error.
 *
 * @author Braydon
 */
@NoArgsConstructor @Setter @Getter @ToString
public final class ErrorResponse {
    /**
     * The status code of this error.
     */
    @NonNull private HttpStatus status;

    /**
     * The message of this error.
     */
    @NonNull private String message;

    /**
     * The timestamp this error occurred.
     */
    private Date timestamp;

    public ErrorResponse(@NonNull HttpStatus status, @NonNull String message) {
        this.status = status;
        this.message = message;
        timestamp = new Date();
    }
}