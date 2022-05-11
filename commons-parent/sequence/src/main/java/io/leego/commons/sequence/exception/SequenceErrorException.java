package io.leego.commons.sequence.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class SequenceErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1962464253981866704L;

    /**
     * Constructs a new sequence error exception with no detail message.
     */
    public SequenceErrorException() {
        super();
    }

    /**
     * Constructs a new sequence error exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SequenceErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a new sequence error exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public SequenceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new sequence error exception with the cause.
     *
     * @param cause the cause.
     */
    public SequenceErrorException(Throwable cause) {
        super(cause);
    }

}
