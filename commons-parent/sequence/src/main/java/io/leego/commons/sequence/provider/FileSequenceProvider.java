package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceErrorException;
import io.leego.commons.sequence.exception.SequenceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leego Yih
 */
public class FileSequenceProvider extends AbstractSequenceProvider {
    private static final Logger logger = LoggerFactory.getLogger(FileSequenceProvider.class);
    protected static final String FILE_EXTENSION = ".seq";
    protected final ConcurrentMap<String, AtomicSequence> store = new ConcurrentHashMap<>(64);
    protected final int retries;
    protected final String directory;

    public FileSequenceProvider(String directory) {
        this.retries = 10;
        this.directory = directory;
        this.loadAll();
    }

    public FileSequenceProvider(int retries, String directory) {
        this.retries = retries;
        this.directory = directory;
        this.loadAll();
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
            write(key, newValue, increment);
            if (value.compareAndSet(expectedValue, newValue)) {
                return new Segment(expectedValue + increment, newValue, increment);
            }
        }
        throw new SequenceErrorException("Failed to modify sequence '" + key + "', after retrying " + i + " time(s) in " + (System.currentTimeMillis() - timestamp) + " ms");
    }

    @Override
    public boolean create(String key, Long value, Integer increment) {
        store.computeIfAbsent(key, k -> {
            write(key, value, increment);
            return new AtomicSequence(value, increment);
        });
        return true;
    }

    @Override
    public boolean update(String key, Integer increment) {
        AtomicSequence seq = store.get(key);
        if (seq == null) {
            return false;
        }
        write(key, seq.getValue().get(), increment);
        seq.setIncrement(increment);
        return true;
    }

    protected void loadAll() {
        File dir = new File(this.directory);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new SequenceErrorException("Failed to create the directory '" + this.directory + "'");
            }
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String name = file.getName();
            if (!name.endsWith(FILE_EXTENSION)) {
                continue;
            }
            String key = name.substring(0, name.length() - FILE_EXTENSION.length());
            byte[] bytes = new byte[8 + 4];
            try (InputStream inputStream = new FileInputStream(file)) {
                if (inputStream.read(bytes) > 0) {
                    this.store.putIfAbsent(key, toSeq(bytes));
                } else {
                    logger.error("Failed to read file '{}'", file.getAbsolutePath());
                }
            } catch (IOException e) {
                logger.error("Failed to open file '{}'", file.getAbsolutePath(), e);
            }
        }
    }

    protected void write(String key, long value, int increment) {
        File file = new File(this.directory + "/" + key + FILE_EXTENSION);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(toBytes(value, increment));
        } catch (IOException e) {
            logger.error("Failed to write file '{}'", file.getAbsolutePath(), e);
        }
    }

    protected AtomicSequence toSeq(byte[] bytes) {
        long value = (long) bytes[0] << 56
                | (long) bytes[1] << 48
                | (long) bytes[2] << 40
                | (long) bytes[3] << 32
                | bytes[4] << 24
                | bytes[5] << 16
                | bytes[6] << 8
                | bytes[7];
        int increment = bytes[8] << 24
                | bytes[9] << 16
                | bytes[10] << 8
                | bytes[11];
        return new AtomicSequence(value, increment);
    }

    protected byte[] toBytes(long value, int increment) {
        byte[] bytes = new byte[8 + 4];
        bytes[0] = (byte) ((value >> 56) & 0xFF);
        bytes[1] = (byte) ((value >> 48) & 0xFF);
        bytes[2] = (byte) ((value >> 40) & 0xFF);
        bytes[3] = (byte) ((value >> 32) & 0xFF);
        bytes[4] = (byte) ((value >> 24) & 0xFF);
        bytes[5] = (byte) ((value >> 16) & 0xFF);
        bytes[6] = (byte) ((value >> 8) & 0xFF);
        bytes[7] = (byte) (value & 0xFF);
        bytes[8] = (byte) ((increment >> 24) & 0xFF);
        bytes[9] = (byte) ((increment >> 16) & 0xFF);
        bytes[10] = (byte) ((increment >> 8) & 0xFF);
        bytes[11] = (byte) (increment & 0xFF);
        return bytes;
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
