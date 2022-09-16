package io.leego.support.jpa.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Leego Yih
 */
public interface Deletable<ID> extends Entity<ID> {

    ID getDeleted();

    void setDeleted(ID deleted);

    LocalDateTime getDeletedTime();

    void setDeletedTime(LocalDateTime deletedTime);

    default boolean isDeleted() {
        return Objects.equals(getDeleted(), getId());
    }

}
