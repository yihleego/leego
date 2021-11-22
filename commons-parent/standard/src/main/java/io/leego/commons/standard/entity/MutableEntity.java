package io.leego.commons.standard.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public abstract class MutableEntity<T> implements IdentifiableEntity<T>, CreatableEntity, UpdatableEntity {
    protected T id;
    protected LocalDateTime createdTime;
    protected LocalDateTime updatedTime;

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

    @Override
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
