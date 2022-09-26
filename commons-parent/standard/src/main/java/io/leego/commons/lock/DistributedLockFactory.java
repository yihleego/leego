package io.leego.commons.lock;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Leego Yih
 */
public class DistributedLockFactory {
    private final ScheduledExecutorService executor;
    private final DistributedAccessor accessor;
    private final String lockPrefix;

    public DistributedLockFactory(ScheduledExecutorService executor, DistributedAccessor accessor, String lockPrefix) {
        this.executor = executor;
        this.accessor = accessor;
        this.lockPrefix = lockPrefix;
    }

    /**
     * Returns a new {@code DistributedLock} with the given condition.
     */
    public DistributedLock create(String condition) {
        return new DistributedLock(executor, accessor, lockPrefix + ":" + condition);
    }
}
