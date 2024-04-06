package me.braydon.mc.exception;

import lombok.NonNull;
import me.braydon.mc.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for handling raised exceptions.
 *
 * @author Braydon
 */
@ControllerAdvice
public final class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(@NonNull Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Get the HTTP status
        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) { // Get from the @ResponseStatus annotation
            status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        }
        String message = ex.getLocalizedMessage(); // Get the error message
        if (message == null) { // Fallback
            message = "An internal error has occurred.";
        }
        return new ResponseEntity<>(new ErrorResponse(status, message), status);
    }
}