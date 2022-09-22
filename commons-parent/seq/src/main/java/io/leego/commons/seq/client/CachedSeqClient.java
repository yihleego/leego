package io.leego.commons.seq.client;

import io.leego.commons.seq.provider.SeqProvider;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public class CachedSeqClient implements SeqClient {
    private final SeqProvider provider;
    private final ConcurrentMap<String, CachedSeq> cache = new ConcurrentHashMap<>(32);
    private final int capacity;
    private final float loadFactor;
    private final Duration timeout;

    public CachedSeqClient(SeqProvider provider, int capacity) {
        this(provider, capacity, 0.75F, Duration.ofSeconds(1));
    }

    public CachedSeqClient(SeqProvider provider, int capacity, float loadFactor, Duration timeout) {
        this.provider = provider;
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.timeout = timeout;
    }

    @Override
    public long next(String key) {
        return getSeq(key).next();
    }

    @Override
    public LinkedList<Long> next(String key, int size) {
        return getSeq(key).next(size);
    }

    @Override
    public <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory) {
        return getSeq(key).next(size, collectionFactory);
    }

    protected CachedSeq getSeq(String key) {
        return cache.computeIfAbsent(key, k -> new CachedSeq(provider, key, capacity, loadFactor, timeout.toMillis()));
    }
}

