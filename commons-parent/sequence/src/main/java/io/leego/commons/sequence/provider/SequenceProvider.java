package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;

/**
 * @author Leego Yih
 */
public interface SequenceProvider {

    /**
     * Obtains the next sequence.
     *
     * @param key the sequence key.
     * @return the next sequence.
     */
    Long next(String key);

    /**
     * Obtains the next segment.
     *
     * @param key  the sequence key.
     * @param size the size.
     * @return the next segment.
     */
    Segment next(String key, int size);

}
