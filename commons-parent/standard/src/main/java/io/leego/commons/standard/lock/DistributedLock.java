package io.leego.commons.standard.lock;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leego Yih
 */
public class DistributedLock {
    private final ScheduledExecutorService executor;
    private final DistributedAccessor accessor;
    private final String key;
    private final String value;
    private final long lockout;
    private final AtomicLong duration;
    private ScheduledFuture<?> future;

    public DistributedLock(ScheduledExecutorService executor, DistributedAccessor accessor, String key) {
        this(executor, accessor, key, 10 * 1000);
    }

    public DistributedLock(ScheduledExecutorService executor, DistributedAccessor accessor, String key, long lockout) {
        this.executor = executor;
        this.accessor = accessor;
        this.key = key;
        this.value = UUID.randomUUID().toString();
        this.lockout = lockout;
        this.duration = new AtomicLong(0);
    }

    /**
     * Acquires the lock.
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     */
    public void lock() throws InterruptedException {
        lock(-1L);
    }

    /**
     * Acquires the lock and set timeout in milliseconds.
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     */
    public void lock(long timeout) throws InterruptedException {
        while (!tryLock(timeout)) {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10, 100));
        }
    }

    /**
     * Acquires the lock only if it is free at the time of invocation.
     *
     * <p>Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
     *
     * <p>A typical usage idiom for this method would be:
     * <pre> {@code
     * DistributedLock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     * <p>
     * This usage ensures that the lock is unlocked if it was acquired, and
     * doesn't try to unlock if the lock was not acquired.
     *
     * @return {@code true} if the lock was acquired and
     * {@code false} otherwise
     */
    public boolean tryLock() {
        return tryLock(-1L);
    }

    /**
     * Acquires the lock and set timeout in milliseconds only if it is free at the time of invocation.
     *
     * <p>Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
     *
     * <p>A typical usage idiom for this method would be:
     * <pre> {@code
     * DistributedLock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     * <p>
     * This usage ensures that the lock is unlocked if it was acquired, and
     * doesn't try to unlock if the lock was not acquired.
     *
     * @return {@code true} if the lock was acquired and
     * {@code false} otherwise
     */
    public boolean tryLock(long timeout) {
        boolean locked = accessor.setIfAbsent(key, value, lockout, TimeUnit.MILLISECONDS);
        if (!locked) {
            return false;
        }
        this.future = executor.scheduleAtFixedRate(() -> {
            if (timeout < 0 || duration.addAndGet(lockout) < timeout) {
                accessor.expire(key, lockout << 1, TimeUnit.MILLISECONDS);
            }
        }, lockout, lockout, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * Releases the lock.
     */
    public void unlock() {
        if (future != null) {
            future.cancel(true);
        }
        accessor.delete(key, value);
    }
}
