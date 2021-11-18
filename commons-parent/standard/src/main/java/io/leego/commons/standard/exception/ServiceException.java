package io.leego.commons.standard.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new service exception with no detail message.
     */
    public ServiceException() {
        super("Oops! An error has occurred.");
    }

    /**
     * Constructs a new service exception with the specified detail message.
     * @param message the detail message.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new service exception with the specified detail message and cause.
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new service exception with the cause.
     * @param cause the cause.
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

}