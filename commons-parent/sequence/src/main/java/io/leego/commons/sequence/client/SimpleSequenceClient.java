package io.leego.commons.sequence.client;

import io.leego.commons.sequence.provider.SequenceProvider;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public class SimpleSequenceClient extends AbstractSequenceClient {

    public SimpleSequenceClient(SequenceProvider sequenceProvider) {
        super(sequenceProvider);
    }

    @Override
    public Long next(String key) {
        return sequenceProvider.next(key);
    }

    @Override
    public <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory) {
        if (size <= 0) {
            return collectionFactory.get();
        }
        return toCollection(sequenceProvider.next(key, size), collectionFactory);
    }
}
