package io.leego.support.jpa.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Creatable {

    LocalDateTime getCreatedTime();

    void setCreatedTime(LocalDateTime createdTime);

}
