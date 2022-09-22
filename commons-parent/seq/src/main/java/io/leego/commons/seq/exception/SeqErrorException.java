package io.leego.commons.seq.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class SeqErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1962464253981866704L;

    /**
     * Constructs a new sequence error exception with no detail message.
     */
    public SeqErrorException() {
        super();
    }

    /**
     * Constructs a new sequence error exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SeqErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a new sequence error exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public SeqErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new sequence error exception with the cause.
     *
     * @param cause the cause.
     */
    public SeqErrorException(Throwable cause) {
        super(cause);
    }

}
