package io.leego.commons.standard.concurrent;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leego Yih
 */
public class NamedThreadFactory implements ThreadFactory {
    private final AtomicLong threadNumber = new AtomicLong(0);
    private final ThreadGroup group;
    private final String namePrefix;
    private final boolean daemon;

    public NamedThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    public NamedThreadFactory(String groupName, String namePrefix) {
        this(groupName, namePrefix, false);
    }

    public NamedThreadFactory(String namePrefix, boolean daemon) {
        this.group = null;
        this.namePrefix = Objects.requireNonNull(namePrefix);
        this.daemon = daemon;
    }

    public NamedThreadFactory(String groupName, String namePrefix, boolean daemon) {
        this.group = new ThreadGroup(Objects.requireNonNull(groupName));
        this.namePrefix = Objects.requireNonNull(namePrefix);
        this.daemon = daemon;
    }

    public NamedThreadFactory(ThreadGroup group, String namePrefix, boolean daemon) {
        this.group = group;
        this.namePrefix = Objects.requireNonNull(namePrefix);
        this.daemon = daemon;
    }

    private String nextName() {
        if (group != null) {
            return group.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement();
        } else {
            return namePrefix + "-" + threadNumber.getAndIncrement();
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, nextName());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

}
