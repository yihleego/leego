package io.leego.support.mongodb.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Creatable<ID> extends Entity<ID> {

    LocalDateTime getCreatedTime();

    void setCreatedTime(LocalDateTime createdTime);

}
