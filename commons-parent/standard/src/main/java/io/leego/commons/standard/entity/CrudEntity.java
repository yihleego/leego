package io.leego.commons.standard.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public abstract class CrudEntity<T> implements IdentifiableEntity<T>, CreatableEntity, UpdatableEntity, DeletableEntity {
    protected T id;
    protected boolean deleted;
    protected LocalDateTime createdTime;
    protected LocalDateTime updatedTime;
    protected LocalDateTime deletedTime;

    @Override
    public T getId() {
        return id;
    }

    @Override
    public void setId(T id) {
        this.id = id;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    @Override
    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }
}
