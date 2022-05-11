package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceErrorException;
import io.leego.commons.sequence.exception.SequenceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leego Yih
 */
public class MemorySequenceProvider extends AbstractSequenceProvider {
    private static final Logger logger = LoggerFactory.getLogger(MemorySequenceProvider.class);
    protected final ConcurrentMap<String, AtomicSequence> store = new ConcurrentHashMap<>(64);
    protected final int retries;

    public MemorySequenceProvider() {
        this.retries = 10;
    }

    public MemorySequenceProvider(int retries) {
        this.retries = Math.max(retries, 1);
    }

    @Override
    public Segment next(String key, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be greater than zero");
        }
        long timestamp = System.currentTimeMillis();
        int i = 1;
        for (; i <= retries; i++) {
            AtomicSequence sequence = this.store.get(key);
            if (sequence == null) {
                throw new SequenceNotFoundException("There is no sequence '" + key + "'");
            }
            AtomicLong value = sequence.getValue();
            int increment = sequence.getIncrement();
            long expectedValue = value.get();
            long newValue = expectedValue + (long) increment * size;
            if (value.compareAndSet(expectedValue, newValue)) {
                return new Segment(expectedValue + increment, newValue, increment);
            }
        }
        throw new SequenceErrorException("Failed to modify sequence '" + key + "', after retrying " + i + " time(s) in " + (System.currentTimeMillis() - timestamp) + " ms");
    }

    @Override
    public boolean create(String key, Long value, Integer increment) {
        store.computeIfAbsent(key, k -> new AtomicSequence(value, increment));
        return true;
    }

    @Override
    public boolean update(String key, Integer increment) {
        AtomicSequence seq = store.get(key);
        if (seq == null) {
            logger.warn("Failed to update sequence '" + key + "' because it does not exist");
            return false;
        }
        seq.setIncrement(increment);
        return true;
    }

    protected static class AtomicSequence {
        private final AtomicLong value;
        private volatile int increment;

        public AtomicSequence(long value, int increment) {
            this.value = new AtomicLong(value);
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
