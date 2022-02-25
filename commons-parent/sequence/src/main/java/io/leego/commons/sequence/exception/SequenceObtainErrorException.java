package io.leego.commons.sequence.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class SequenceObtainErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1962464253981866704L;

    /**
     * Constructs a new sequence obtain error exception with no detail message.
     */
    public SequenceObtainErrorException() {
        super();
    }

    /**
     * Constructs a new sequence obtain error exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SequenceObtainErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a new sequence obtain error exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public SequenceObtainErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new sequence obtain error exception with the cause.
     *
     * @param cause the cause.
     */
    public SequenceObtainErrorException(Throwable cause) {
        super(cause);
    }

}
