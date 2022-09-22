package io.leego.commons.seq.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class SeqNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3598006951231519508L;

    /**
     * Constructs a new sequence not found exception with no detail message.
     */
    public SeqNotFoundException() {
        super();
    }

    /**
     * Constructs a new sequence not found exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SeqNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new sequence not found exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public SeqNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new sequence not found exception with the cause.
     *
     * @param cause the cause.
     */
    public SeqNotFoundException(Throwable cause) {
        super(cause);
    }

}
