package io.leego.commons.standard.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public abstract class ImmutableEntity<T> implements IdentifiableEntity<T>, CreatableEntity {
    protected T id;
    protected LocalDateTime createdTime;

    @Override
    public T getId() {
        return id;
    }

    @Override
    public void setId(T id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}

