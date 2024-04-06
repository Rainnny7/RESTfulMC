package me.braydon.mc.exception.impl;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is raised
 * when a bad request is made.
 *
 * @author Braydon
 */
@StandardException
@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class BadRequestException extends RuntimeException { }