package io.leego.commons.bean;

import java.util.Map;
import java.util.Objects;

/**
 * @author Leego Yih
 */
public abstract class AbstractSingletonBeanFactory<B, K> implements SingletonBeanFactory<B, K> {
    protected final Map<String, B> beans;

    public AbstractSingletonBeanFactory(Map<String, B> beans) {
        this.beans = beans;
    }

    @Override
    public B get(K key) {
        Objects.requireNonNull(key);
        return beans.get(getBeanName(key));
    }

    @Override
    public boolean contains(K key) {
        return beans.containsKey(getBeanName(key));
    }
}
