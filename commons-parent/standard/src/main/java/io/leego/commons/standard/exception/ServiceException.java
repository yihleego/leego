package io.leego.commons.standard.exception;

import io.leego.commons.standard.Error;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    protected final Integer code;

    /**
     * Constructs a new service exception with no detail message.
     */
    public ServiceException() {
        super("Oops! An error has occurred.");
        this.code = 0;
    }

    /**
     * Constructs a new service exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ServiceException(String message) {
        super(message);
        this.code = 0;
    }

    /**
     * Constructs a new service exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = 0;
    }

    /**
     * Constructs a new service exception with the cause.
     *
     * @param cause the cause.
     */
    public ServiceException(Throwable cause) {
        super(cause);
        this.code = 0;
    }

    /**
     * Constructs a new service exception with the error code and specified detail message.
     *
     * @param code    the error code.
     * @param message the detail message.
     */
    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Constructs a new service exception with the error code and cause.
     *
     * @param code  the error code.
     * @param cause the cause.
     */
    public ServiceException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Constructs a new service exception with the error code, specified detail message and cause.
     *
     * @param code    the error code.
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Constructs a new service exception with the error.
     *
     * @param error the error.
     */
    public ServiceException(Error error) {
        this(error.getCode(), error.getMessage());
    }

    /**
     * Constructs a new service exception with the error and cause.
     *
     * @param error the error.
     * @param cause the cause.
     */
    public ServiceException(Error error, Throwable cause) {
        this(error.getCode(), error.getMessage(), cause);
    }

    /**
     * Returns the error code.
     */
    public Integer getCode() {
        return code;
    }
}