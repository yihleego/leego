package io.leego.commons.standard.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface DeletableEntity {

    boolean isDeleted();

    void setDeleted(boolean deleted);

    LocalDateTime getDeletedTime();

    void setDeletedTime(LocalDateTime deletedTime);

}
