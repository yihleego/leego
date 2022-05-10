package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceNotFoundException;
import io.leego.commons.sequence.exception.SequenceObtainErrorException;

/**
 * @author Leego Yih
 */
public abstract class AbstractSequenceProvider implements SequenceProvider {
    protected final int retries;

    public AbstractSequenceProvider() {
        this(10);
    }

    public AbstractSequenceProvider(int retries) {
        this.retries = Math.max(retries, 1);
    }

    @Override
    public Long next(String key) {
        return this.next(key, 1).getBegin();
    }

    @Override
    public Segment next(String key, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be greater than zero");
        }
        long timestamp = System.currentTimeMillis();
        int i = 1;
        for (; i <= retries; i++) {
            Sequence sequence = this.find(key);
            if (sequence == null) {
                throw new SequenceNotFoundException("There is no sequence '" + key + "'");
            }
            int increment = sequence.getIncrement();
            long expectedValue = sequence.getValue();
            long newValue = expectedValue + (long) increment * size;
            int version = sequence.getVersion();
            if (this.compareAndSet(key, expectedValue, newValue, version)) {
                return new Segment(expectedValue + increment, newValue, increment);
            }
        }
        throw new SequenceObtainErrorException("Failed to modify sequence '" + key + "', after retrying " + i + " time(s) in " + (System.currentTimeMillis() - timestamp) + " ms");
    }

    @Override
    public abstract boolean create(String key, Long value, Integer increment);

    @Override
    public abstract boolean update(String key, Integer increment);

    protected abstract Sequence find(String key);

    protected abstract boolean compareAndSet(String key, long expectedValue, long newValue, int version);

    protected static class Sequence {
        private String key;
        private Long value;
        private Integer increment;
        private Integer version;

        public Sequence() {
        }

        public Sequence(String key, Long value, Integer increment, Integer version) {
            this.key = key;
            this.value = value;
            this.increment = increment;
            this.version = version;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public Integer getIncrement() {
            return increment;
        }

        public void setIncrement(Integer increment) {
            this.increment = increment;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }
    }
}
