package io.leego.commons.sequence.client;

import io.leego.commons.sequence.provider.SequenceProvider;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public abstract class AbstractSequenceClient implements SequenceClient {
    protected SequenceProvider sequenceProvider;

    public AbstractSequenceClient(SequenceProvider sequenceProvider) {
        this.sequenceProvider = Objects.requireNonNull(sequenceProvider);
    }

    @Override
    public abstract Long next(String key);

    @Override
    public LinkedList<Long> next(String key, int size) {
        return next(key, size, LinkedList::new);
    }

    @Override
    public abstract <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory);

    public SequenceProvider getSequenceProvider() {
        return sequenceProvider;
    }

    protected void setSequenceProvider(SequenceProvider sequenceProvider) {
        this.sequenceProvider = sequenceProvider;
    }
}
