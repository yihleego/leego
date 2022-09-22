package io.leego.commons.seq.provider;

import io.leego.commons.seq.exception.SeqNotFoundException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leego Yih
 */
public class MemorySeqProvider implements SeqProvider {
    protected final ConcurrentMap<String, AtomicSeq> store = new ConcurrentHashMap<>(64);

    @Override
    public long next(String key) {
        return getSeq(key).incrementAndGet();
    }

    @Override
    public Segment next(String key, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }
        AtomicSeq seq = getSeq(key);
        int increment = seq.increment;
        long delta = (long) increment * size;
        long newValue = seq.addAndGet(delta);
        return new Segment(newValue - delta, increment, size);
    }

    @Override
    public boolean create(String key, long value, int increment) {
        return store.putIfAbsent(key, new AtomicSeq(value, increment)) == null;
    }

    @Override
    public boolean update(String key, int increment) {
        AtomicSeq seq = store.get(key);
        if (seq == null) {
            return false;
        }
        seq.increment = increment;
        return true;
    }

    @Override
    public boolean contains(String key) {
        return store.containsKey(key);
    }

    private AtomicSeq getSeq(String key) {
        AtomicSeq seq = this.store.get(key);
        if (seq == null) {
            throw new SeqNotFoundException("Missing '" + key + "'");
        }
        return seq;
    }

    private class AtomicSeq {
        final AtomicLong value;
        volatile int increment;

        AtomicSeq(long value, int increment) {
            this.value = new AtomicLong(value);
            this.increment = increment;
        }

        long incrementAndGet() {
            return value.addAndGet(increment);
        }

        long addAndGet(long delta) {
            return value.addAndGet(delta);
        }
    }
}
