package io.leego.commons.standard.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class ServiceUnavailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with no detail message.
     */
    public ServiceUnavailableException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ServiceUnavailableException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the cause.
     *
     * @param cause the cause.
     */
    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }
}