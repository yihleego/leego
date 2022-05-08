package io.leego.commons.sequence;

import java.io.Serial;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A globally, 64 bits, thread-safe identifier. It can generate 4,194,303 numbers per second.
 * <pre>
 * ┌--------┬--------┬--------┬--------┬--------┬--------┬--------┬--------┐
 * |11111111|11111111|11111111|11111111|11111111|11111111|11111111|11111111| FORMAT: 64 bits
 * ├--------┼--------┼--------┼--------┼--------┼--------┼--------┼--------┤
 * |XXXXXXXX|XXXXXXXX|XXXXXXXX|XXXXXXXX|        |        |        |        | TIMESTAMP: 32 bits
 * ├--------┼--------┼--------┼--------┼--------┼--------┼--------┼--------┤
 * |        |        |        |        |XXXXXXXX|XX      |        |        | WORKER ID: 10 bits
 * ├--------┼--------┼--------┼--------┼--------┼--------┼--------┼--------┤
 * |        |        |        |        |        |  XXXXXX|XXXXXXXX|XXXXXXXX| SEQUENCE: 22 bits
 * └--------┴--------┴--------┴--------┴--------┴--------┴--------┴--------┘
 * </pre>
 *
 * @author Leego Yih
 */
public class Seq implements Serializable {
    @Serial
    private static final long serialVersionUID = -8895610628733987794L;
    /**
     * Unix timestamp offset.
     * 2020-01-01 00:00:00 UTC
     */
    private static final long OFFSET = 1577836800L;
    /**
     * Maximum worker-id is 1023.
     */
    private static final int MAX_WORKER_ID = (1 << 10) - 1;
    /**
     * Maximum sequence is 4194303.
     */
    private static final int MAX_SEQUENCE = (1 << 22) - 1;
    /**
     * Adder with initial random number.
     */
    private static final AtomicInteger ADDER = new AtomicInteger(new SecureRandom().nextInt());
    /**
     * The worker identifier.
     */
    private final int workerId;

    /**
     * Constructs a new {@code Seq}.
     */
    public Seq(int workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("The worker identifier must be between 0 and 1023.");
        }
        this.workerId = workerId;
    }

    /**
     * Returns a new {@code sequence}.
     *
     * @return a new {@code sequence}.
     */
    public long next() {
        long t = System.currentTimeMillis() / 1000 - OFFSET;
        long w = workerId;
        int v = ADDER.getAndIncrement() & MAX_SEQUENCE;
        return t << 32 | (w & 0x3FF) << 22 | v & 0x3FFFFF;
    }

    /**
     * Returns a new {@code Seq} with the worker identifier,
     * and the worker identifier must be between 0 and 1023.
     *
     * @param workerId the worker identifier.
     * @return a new {@code Seq}.
     */
    public static Seq create(int workerId) {
        return new Seq(workerId);
    }

    /**
     * Returns a new {@code Seq} with a random worker identifier.
     *
     * @return a new {@code Seq}.
     */
    public static Seq random() {
        return new Seq(new SecureRandom().nextInt(MAX_WORKER_ID));
    }
}
