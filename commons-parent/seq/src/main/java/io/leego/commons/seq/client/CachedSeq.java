package io.leego.commons.seq.client;

import io.leego.commons.seq.exception.SeqErrorException;
import io.leego.commons.seq.exception.SeqNotFoundException;
import io.leego.commons.seq.exception.SeqTimeoutException;
import io.leego.commons.seq.provider.SeqProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
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
public class CachedSeq implements Seq {
    private static final Logger logger = LoggerFactory.getLogger(CachedSeq.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2,
            new DefaultThreadFactory());
    private final SeqProvider provider;
    private final String key;
    private final int capacity;
    private final int threshold;
    private final float loadFactor;
    /** timeout in milliseconds */
    private final long timeout;
    private final BlockingQueue<Long> queue;
    private volatile boolean syncing;

    public CachedSeq(SeqProvider provider, String key, int capacity, float loadFactor, long timeout) {
        if (!provider.contains(key)) {
            throw new SeqNotFoundException("Seq '" + key + "' cannot be found");
        }
        this.provider = provider;
        this.key = key;
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.queue = new LinkedBlockingQueue<>();
        this.timeout = timeout;
    }

    @Override
    public long next() {
        try {
            Long v = queue.poll();
            if (v == null) {
                trySync();
                v = queue.poll(timeout, TimeUnit.MILLISECONDS);
            }
            if (v != null) {
                return v;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SeqErrorException(e);
        }
        throw new SeqTimeoutException("Obtain \"" + key + "\" timeout");
    }

    @Override
    public LinkedList<Long> next(int size) {
        return this.next(size, LinkedList::new);
    }

    @Override
    public <C extends Collection<Long>> C next(int size, Supplier<C> collectionFactory) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }
        C c = collectionFactory.get();
        try {
            for (int i = 0; i < size; i++) {
                Long v = queue.poll();
                if (v == null) {
                    trySync();
                    v = queue.poll(timeout, TimeUnit.MILLISECONDS);
                }
                if (v != null) {
                    c.add(v);
                } else {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SeqErrorException(e);
        }
        if (c.size() == size) {
            return c;
        }
        throw new SeqTimeoutException("Obtain \"" + key + "\" timeout");
    }

    public void trySync() {
        if (!isSyncable()) return;
        executorService.execute(() -> {
            if (!isSyncable()) return;
            synchronized (this) {
                if (!isSyncable()) return;
                try {
                    syncing = true;
                    provider.next(key, threshold).forEach(queue::offer);
                } catch (Throwable t) {
                    logger.error("", t);
                } finally {
                    syncing = false;
                }
            }
        });
    }

    public boolean isSyncable() {
        return !syncing && queue.size() < threshold;
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

    public float getLoadFactor() {
        return loadFactor;
    }

    public long getTimeout() {
        return timeout;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        final AtomicLong threadNumber = new AtomicLong(0);
        final ThreadGroup group = new ThreadGroup("seq");

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(group, r, "seq-syncing-" + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }
}
