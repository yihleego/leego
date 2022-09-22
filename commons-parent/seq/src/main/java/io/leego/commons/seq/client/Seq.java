package io.leego.commons.seq.client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public interface Seq {

    /**
     * Obtains the next sequence.
     *
     * @return the next sequence.
     */
    long next();

    /**
     * Obtains the next segment of the sequence.
     *
     * @param size the size.
     * @return the next segment of the sequence.
     */
    LinkedList<Long> next(int size);

    /**
     * Obtains the next segment of the sequence.
     *
     * @param size              the size.
     * @param collectionFactory a supplier providing a new empty {@code Collection}
     *                          into which the sequences will be inserted.
     * @return the next segment of the sequence.
     */
    <C extends Collection<Long>> C next(int size, Supplier<C> collectionFactory);

}
