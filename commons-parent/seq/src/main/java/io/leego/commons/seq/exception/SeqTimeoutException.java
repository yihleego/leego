package io.leego.commons.seq.exception;

import java.io.Serial;

/**
 * @author Leego Yih
 */
public class SeqTimeoutException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6121026105551101465L;

    /**
     * Constructs a new sequence timeout exception with no detail message.
     */
    public SeqTimeoutException() {
        super();
    }

    /**
     * Constructs a new sequence timeout exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SeqTimeoutException(String message) {
        super(message);
    }

    /**
     * Constructs a new sequence timeout exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause   the cause.
     */
    public SeqTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new sequence timeout exception with the cause.
     *
     * @param cause the cause.
     */
    public SeqTimeoutException(Throwable cause) {
        super(cause);
    }

}
