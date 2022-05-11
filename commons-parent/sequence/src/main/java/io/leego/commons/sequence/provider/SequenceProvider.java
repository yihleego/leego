package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;

/**
 * @author Leego Yih
 */
public interface SequenceProvider {

    /**
     * Obtains the next sequence.
     *
     * @param key the key of the sequence.
     * @return the next sequence.
     */
    Long next(String key);

    /**
     * Obtains the next segment.
     *
     * @param key  the key of the sequence.
     * @param size the size to be obtained
     * @return the next segment.
     */
    Segment next(String key, int size);

    /**
     * Creates a sequence.
     *
     * @param key       the key of the sequence.
     * @param value     the initialized value of the sequence.
     * @param increment the increment of the sequence.
     * @return <code>true</code> if the sequence is created.
     */
    boolean create(String key, Long value, Integer increment);

    /**
     * Updates a sequence.
     *
     * @param key       the key of the sequence.
     * @param increment the increment of the sequence.
     * @return <code>true</code> if the sequence is updated.
     */
    boolean update(String key, Integer increment);

    /**
     * Creates a sequence with the value of 0 and the increment of 1.
     *
     * @param key the key of the sequence.
     * @return <code>true</code> if the sequence is created.
     */
    default boolean create(String key) {
        return this.create(key, 0L, 1);
    }

}
