package cc.restfulmc.api.exception.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is raised when the
 * Mojang API rate limit is reached.
 *
 * @author Braydon
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public final class MojangRateLimitException extends RuntimeException {
    public MojangRateLimitException() {
        super("Mojang requests exhausted, please try again later.");
    }
}