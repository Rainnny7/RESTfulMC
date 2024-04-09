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
package me.braydon.mc.model.response;

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