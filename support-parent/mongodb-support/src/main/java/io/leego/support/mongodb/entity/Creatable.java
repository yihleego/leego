package io.leego.support.mongodb.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Creatable {

    LocalDateTime getCreatedTime();

    void setCreatedTime(LocalDateTime createdTime);

}
