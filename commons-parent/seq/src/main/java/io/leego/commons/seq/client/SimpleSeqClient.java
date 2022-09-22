package io.leego.commons.seq.client;

import io.leego.commons.seq.provider.SeqProvider;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public class SimpleSeqClient implements SeqClient {
    private final SeqProvider provider;

    public SimpleSeqClient(SeqProvider provider) {
        this.provider = provider;
    }

    @Override
    public long next(String key) {
        return provider.next(key);
    }

    @Override
    public LinkedList<Long> next(String key, int size) {
        return provider.next(key, size).toCollection(LinkedList::new);
    }

    @Override
    public <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory) {
        return provider.next(key, size).toCollection(collectionFactory);
    }
}
