package io.leego.support.jpa.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Deletable<DEL> {

    DEL getDeleted();

    void setDeleted(DEL deleted);

    LocalDateTime getDeletedTime();

    void setDeletedTime(LocalDateTime deletedTime);

    boolean isDeleted();

}
