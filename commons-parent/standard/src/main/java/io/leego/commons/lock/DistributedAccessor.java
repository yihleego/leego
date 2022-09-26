package io.leego.commons.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author Leego Yih
 */
public interface DistributedAccessor {

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is absent.
     *
     * @param key     must not be null.
     * @param value   must not be null.
     * @param timeout the key expiration timeout.
     * @param unit    must not be null.
     * @return {@code true} if the key is set.
     */
    boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit);

    /**
     * Set time to live for given {@code key}.
     *
     * @param key     must not be null.
     * @param timeout the key expiration timeout.
     * @param unit    must not be null.
     * @return {@code true} if the key is set.
     */
    boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * Delete given {@code key} and {@code value}.
     *
     * @param key   must not be null.
     * @param value must not be null.
     * @return {@code true} if the key is removed.
     */
    boolean delete(String key, String value);

}
