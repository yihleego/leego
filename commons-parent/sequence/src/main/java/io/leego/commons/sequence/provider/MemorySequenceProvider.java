package io.leego.commons.sequence.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leego Yih
 */
public class MemorySequenceProvider extends AbstractSequenceProvider {
    protected final ConcurrentMap<String, AtomicSequence> store = new ConcurrentHashMap<>(64);

    public MemorySequenceProvider() {
    }

    public MemorySequenceProvider(int retries) {
        super(retries);
    }

    @Override
    public boolean create(String key, Long value, Integer increment) {
        store.computeIfAbsent(key, k -> new AtomicSequence(new AtomicLong(value), increment));
        return true;
    }

    @Override
    public boolean update(String key, Integer increment) {
        AtomicSequence seq = store.get(key);
        if (seq == null) {
            return false;
        }
        seq.setIncrement(increment);
        return true;
    }

    @Override
    protected Sequence find(String key) {
        AtomicSequence seq = store.get(key);
        if (seq == null) {
            return null;
        }
        return new Sequence(key, seq.getValue().get(), seq.getIncrement(), null);
    }

    @Override
    protected boolean compareAndSet(String key, long expectedValue, long newValue, int version) {
        AtomicSequence seq = store.get(key);
        if (seq == null) {
            return false;
        }
        return seq.getValue().compareAndSet(expectedValue, newValue);
    }

    protected static class AtomicSequence {
        private final AtomicLong value;
        private volatile int increment;

        public AtomicSequence(AtomicLong value, int increment) {
            this.value = value;
            this.increment = increment;
        }

        public AtomicLong getValue() {
            return value;
        }

        public int getIncrement() {
            return increment;
        }

        public void setIncrement(int increment) {
            this.increment = increment;
        }
    }
}
