/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny), and the RESTfulMC contributors
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
package me.braydon.mc.exception;

import lombok.NonNull;
import me.braydon.mc.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Advice for handling raised exceptions.
 *
 * @author Braydon
 */
@ControllerAdvice
public final class ExceptionControllerAdvice {
    /**
     * Handle a raised exception.
     *
     * @param ex the raised exception
     * @return the error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(@NonNull Exception ex) {
        HttpStatus status = null; // Get the HTTP status
        if (ex instanceof NoResourceFoundException) { // Not found
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof UnsupportedOperationException) { // Not implemented
            status = HttpStatus.NOT_IMPLEMENTED;
        }
        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) { // Get from the @ResponseStatus annotation
            status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        }
        String message = ex.getLocalizedMessage(); // Get the error message
        if (message == null) { // Fallback
            message = "An internal error has occurred.";
        }
        // Print the stack trace if no response status is present
        if (status == null) {
            ex.printStackTrace();
        }
        if (status == null) { // Fallback to 500
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(new ErrorResponse(status, message), status);
    }
}