package io.leego.commons.sequence.client;

import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceNotFoundException;
import io.leego.commons.sequence.exception.SequenceObtainErrorException;
import io.leego.commons.sequence.exception.SequenceObtainTimeoutException;
import io.leego.commons.sequence.provider.SequenceProvider;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * @author Leego Yih
 */
public class CachedSequenceClient extends AbstractSequenceClient {
    protected static final int CACHE_SIZE = 100;
    protected static final Duration TIMEOUT = Duration.ofSeconds(3L);
    protected static final float FACTOR = 0.1f;
    protected static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory());
    protected final ConcurrentMap<String, CachedSeq> seqMap = new ConcurrentHashMap<>(32);
    protected final int cacheSize;
    protected final Duration timeout;
    protected final float factor;

    public CachedSequenceClient(SequenceProvider sequenceProvider) {
        this(sequenceProvider, CACHE_SIZE, TIMEOUT, FACTOR);
    }

    public CachedSequenceClient(SequenceProvider sequenceProvider, Integer cacheSize) {
        this(sequenceProvider, cacheSize, TIMEOUT, FACTOR);
    }

    public CachedSequenceClient(SequenceProvider sequenceProvider, Integer cacheSize, Duration timeout) {
        this(sequenceProvider, cacheSize, timeout, FACTOR);
    }

    public CachedSequenceClient(SequenceProvider sequenceProvider, Integer cacheSize, Duration timeout, Float factor) {
        super(sequenceProvider);
        this.cacheSize = cacheSize != null ? cacheSize : CACHE_SIZE;
        this.timeout = timeout != null ? timeout : TIMEOUT;
        this.factor = factor != null ? factor : FACTOR;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public Duration getTimeout() {
        return timeout;
    }

    @Override
    public Long next(String key) {
        CachedSeq seq = getSeq(key);
        trySync(seq);
        try {
            Long value = seq.poll();
            if (value != null) {
                return value;
            }
            if (seq.isPresent()) {
                throw new SequenceObtainTimeoutException("Obtain sequence \"" + key + "\" timeout");
            } else {
                throw new SequenceNotFoundException("The sequence \"" + key + "\" was not found");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SequenceObtainErrorException(e);
        }
    }

    @Override
    public <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory) {
        if (size <= 0) {
            return collectionFactory.get();
        }
        C collection = collectionFactory.get();
        CachedSeq seq = getSeq(key);
        trySync(seq);
        try {
            for (int i = 0; i < size; i++) {
                if (seq.isEmpty()) {
                    trySync(seq);
                }
                Long value = seq.poll();
                if (value != null) {
                    collection.add(value);
                } else {
                    if (seq.isPresent()) {
                        throw new SequenceObtainTimeoutException("Obtain sequence \"" + key + "\" timeout");
                    } else {
                        throw new SequenceNotFoundException("The sequence \"" + key + "\" was not found");
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SequenceObtainErrorException(e);
        }
        return collection;
    }

    public void load(final String key) {
        CachedSeq seq = getSeq(key);
        trySync(seq);
    }

    protected CachedSeq getSeq(final String key) {
        return seqMap.computeIfAbsent(key, k -> new CachedSeq(k, cacheSize, factor, timeout.toMillis()));
    }

    protected void trySync(final CachedSeq seq) {
        if (!seq.isSyncable()) {
            return;
        }
        executorService.execute(() -> {
            if (!seq.isSyncable()) {
                return;
            }
            synchronized (seq) {
                if (!seq.isSyncable()) {
                    return;
                }
                try {
                    seq.beginSyncing();
                    Segment s = sequenceProvider.next(seq.getKey(), seq.getCapacity());
                    if (s != null) {
                        seq.ensurePresent();
                        for (long b = s.getBegin(), e = s.getEnd(), i = s.getIncrement(); b <= e; b += i) {
                            seq.offer(b);
                        }
                    }
                } finally {
                    seq.endSyncing();
                }
            }
        });
    }

    protected static final class CachedSeq {
        private final String key;
        private final int capacity;
        private final int threshold;
        /** timeout in milliseconds. */
        private final long timeout;
        private final BlockingQueue<Long> queue;
        private volatile boolean syncing = false;
        private volatile boolean present = false;
        private volatile long timestamp = 0L;

        public CachedSeq(String key, int capacity, float factor, long timeout) {
            if (capacity <= 0 || factor >= 1 || factor < 0) {
                throw new IllegalArgumentException();
            }
            this.key = key;
            this.capacity = capacity;
            this.threshold = (int) (capacity * factor);
            this.timeout = timeout;
            this.queue = new LinkedBlockingQueue<>();
        }

        public boolean offer(Long value) {
            return queue.offer(value);
        }

        public Long poll() throws InterruptedException {
            return queue.poll(timeout, TimeUnit.MILLISECONDS);
        }

        public Long poll(long timeout, TimeUnit unit) throws InterruptedException {
            return queue.poll(timeout, unit);
        }

        public int size() {
            return queue.size();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public boolean isSyncable() {
            return !syncing
                    && queue.size() <= threshold
                    && (present || timestamp + timeout < System.currentTimeMillis());
        }

        public void beginSyncing() {
            this.syncing = true;
        }

        public void endSyncing() {
            this.timestamp = System.currentTimeMillis();
            this.syncing = false;
        }

        public void ensurePresent() {
            if (!this.present) {
                this.present = true;
            }
        }

        public boolean isSyncing() {
            return syncing;
        }

        public boolean isPresent() {
            return present;
        }

        public String getKey() {
            return key;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getThreshold() {
            return threshold;
        }

        public long getLastSyncTime() {
            return timestamp;
        }

        @Override
        public String toString() {
            return "CachedSeq{" +
                    "key='" + key + '\'' +
                    ", capacity=" + capacity +
                    ", threshold=" + threshold +
                    ", timeout=" + timeout +
                    ", queue=" + queue +
                    ", syncing=" + syncing +
                    ", present=" + present +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }

    protected static final class DefaultThreadFactory implements ThreadFactory {
        private static final String GROUP_NAME = "sequence-client";
        private static final String NAME_PREFIX = "syncing";
        private final AtomicLong threadNumber = new AtomicLong(0);
        private final ThreadGroup group = new ThreadGroup(GROUP_NAME);

        @Override
        public Thread newThread(Runnable runnable) {
            String name = group.getName() + "-" + NAME_PREFIX + "-" + threadNumber.getAndIncrement();
            Thread thread = new Thread(group, runnable, name);
            thread.setDaemon(true);
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }
}
