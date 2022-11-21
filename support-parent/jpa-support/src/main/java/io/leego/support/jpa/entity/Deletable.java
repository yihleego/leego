package io.leego.support.jpa.entity;

import java.time.Instant;

/**
 * @author Leego Yih
 */
public interface Deletable<DEL> {

    DEL getDeleted();

    void setDeleted(DEL deleted);

    Instant getDeletedTime();

    void setDeletedTime(Instant deletedTime);

    boolean isDeleted();

}
