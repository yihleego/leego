package io.leego.commons.sequence.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class SequenceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3598006951231519508L;

    /**
     * Constructs a new sequence not found exception with no detail message.
     */
    public SequenceNotFoundException() {
        super();
    }

    /**
     * Constructs a new sequence not found exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SequenceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new sequence not found exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public SequenceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new sequence not found exception with the cause.
     *
     * @param cause the cause.
     */
    public SequenceNotFoundException(Throwable cause) {
        super(cause);
    }

}
