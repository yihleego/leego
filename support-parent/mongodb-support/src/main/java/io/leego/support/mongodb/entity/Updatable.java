package io.leego.support.mongodb.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Updatable<ID> extends Entity<ID> {

    LocalDateTime getUpdatedTime();

    void setUpdatedTime(LocalDateTime updatedTime);

}
