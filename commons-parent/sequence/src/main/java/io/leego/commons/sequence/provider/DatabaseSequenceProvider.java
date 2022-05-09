package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceNotFoundException;
import io.leego.commons.sequence.exception.SequenceObtainErrorException;

/**
 * @author Leego Yih
 */
public abstract class DatabaseSequenceProvider implements SequenceProvider {
    protected final int retries;

    public DatabaseSequenceProvider(int retries) {
        this.retries = retries;
    }

    @Override
    public Long next(String key) {
        // Poor performance with obtaining a single sequence.
        return next(key, 1).getBegin();
    }

    @Override
    public Segment next(String key, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be greater than zero");
        }
        long timestamp = System.currentTimeMillis();
        int i = 0;
        try {
            for (; i < retries; i++) {
                Sequence sequence = this.findById(key);
                if (sequence == null) {
                    throw new SequenceNotFoundException("There is no sequence '" + key + "'");
                }
                int increment = sequence.getIncrement();
                long expectedValue = sequence.getValue();
                long newValue = expectedValue + (long) increment * size;
                int version = sequence.getVersion();
                int affectedRows = this.compareAndUpdateValue(key, expectedValue, newValue, version);
                if (affectedRows > 0) {
                    return new Segment(expectedValue + increment, newValue, increment);
                }
            }
        } catch (Exception e) {
            throw new SequenceObtainErrorException("Failed to obtain sequence '" + key + "'", e);
        }
        throw new SequenceObtainErrorException("Failed to modify sequence '" + key + "', after retrying " + i + " time(s) in " + (System.currentTimeMillis() - timestamp) + " ms");
    }

    @Override
    public boolean create(String key, Long value, Integer increment) {
        try {
            return save(new Sequence(key, value, increment, 1)) > 0;
        } catch (Exception e) {
            throw new SequenceObtainErrorException("Failed to create sequence '" + key + "'", e);
        }
    }

    @Override
    public boolean update(String key, Integer increment) {
        try {
            return update(new Sequence(key, null, increment, null)) > 0;
        } catch (Exception e) {
            throw new SequenceObtainErrorException("Failed to create sequence '" + key + "'", e);
        }
    }

    protected abstract int save(Sequence sequence) throws Exception;

    protected abstract int update(Sequence sequence) throws Exception;

    protected abstract int compareAndUpdateValue(String id, long expectedValue, long newValue, int version) throws Exception;

    protected abstract Sequence findById(String id) throws Exception;

    protected static class Sequence {
        private String id;
        private Long value;
        private Integer increment;
        private Integer version;

        public Sequence() {
        }

        public Sequence(String id, Long value, Integer increment, Integer version) {
            this.id = id;
            this.value = value;
            this.increment = increment;
            this.version = version;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
