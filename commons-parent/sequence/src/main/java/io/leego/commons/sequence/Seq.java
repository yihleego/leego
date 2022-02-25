package io.leego.commons.sequence;

import java.io.Serial;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A globally, 64 bits, unique identifier.
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
    private static final int MAX_WORKER_ID = 0x03FF;
    /**
     * Maximum sequence is 4194303.
     */
    private static final int MAX_SEQUENCE = 0x003F_FFFF;
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
        int t = (int) (System.currentTimeMillis() / 1000 - OFFSET);
        int w = workerId;
        int s = ADDER.getAndIncrement() & MAX_SEQUENCE;
        return fromBytes(
                (byte) (t >> 24),
                (byte) (t >> 16),
                (byte) (t >> 8),
                (byte) (t),
                (byte) (w >> 2),
                (byte) (((w & 0b11) << 6) + ((s >> 16) & 0b11_1111)),
                (byte) (s >> 8),
                (byte) (s));
    }

    private long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return (b1 & 0xFFL) << 56
                | (b2 & 0xFFL) << 48
                | (b3 & 0xFFL) << 40
                | (b4 & 0xFFL) << 32
                | (b5 & 0xFFL) << 24
                | (b6 & 0xFF) << 16
                | (b7 & 0xFF) << 8
                | (b8 & 0xFF);
    }

    /**
     * Returns a new {@code Seq} with the worker identifier,
     * and the worker identifier must be between 0 and 1023..
     *
     * @param workerId the worker identifier.
     * @return a new {@code Seq}.
     */
    public static Seq create(int workerId) {
        return new Seq(workerId);
    }

    /**
     * Returns a new {@code Seq} with a random worker identifier,.
     *
     * @return a new {@code Seq}.
     */
    public static Seq random() {
        return new Seq(new SecureRandom().nextInt(MAX_WORKER_ID));
    }
}
