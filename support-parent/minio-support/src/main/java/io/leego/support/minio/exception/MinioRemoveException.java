package io.leego.support.minio.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class MinioRemoveException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new minio remove exception with no detail message.
     */
    public MinioRemoveException() {
        super();
    }

    /**
     * Constructs a new minio remove exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public MinioRemoveException(String message) {
        super(message);
    }

    /**
     * Constructs a new minio remove exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public MinioRemoveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new minio remove exception with the cause.
     *
     * @param cause the cause.
     */
    public MinioRemoveException(Throwable cause) {
        super(cause);
    }

}