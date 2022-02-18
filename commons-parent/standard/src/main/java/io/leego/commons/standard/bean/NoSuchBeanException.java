package io.leego.commons.standard.bean;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class NoSuchBeanException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with no detail message.
     */
    public NoSuchBeanException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public NoSuchBeanException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public NoSuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the cause.
     *
     * @param cause the cause.
     */
    public NoSuchBeanException(Throwable cause) {
        super(cause);
    }

}