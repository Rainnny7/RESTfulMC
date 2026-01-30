package cc.restfulmc.api.exception.impl;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is raised
 * when a resource is not found.
 *
 * @author Braydon
 */
@StandardException
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException { }