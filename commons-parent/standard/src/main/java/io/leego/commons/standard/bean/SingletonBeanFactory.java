package io.leego.commons.standard.bean;

/**
 * @author Leego Yih
 */
public interface SingletonBeanFactory<B, K> {

    /**
     * Returns the instance to which the specified key is mapped.
     * @param key the key of the bean to get.
     * @return the instance.
     */
    B get(K key);

    /**
     * Returns {@code true} if this bean factory contain a bean definition or externally registered singleton
     * instance with the given name.
     * @param key the key of the bean to query.
     * @return whether a bean with the given name is present.
     */
    boolean contains(K key);

    /**
     * Returns the name.
     * @param key the key of the bean.
     * @return the name.
     */
    default String getBeanName(K key) {
        return String.valueOf(key);
    }

}
