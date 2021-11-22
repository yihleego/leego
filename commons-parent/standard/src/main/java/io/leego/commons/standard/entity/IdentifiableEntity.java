package io.leego.commons.standard.entity;

/**
 * @author Leego Yih
 */
public interface IdentifiableEntity<T> {

    T getId();

    void setId(T id);

}
