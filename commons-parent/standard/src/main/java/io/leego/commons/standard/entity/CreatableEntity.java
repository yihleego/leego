package io.leego.commons.standard.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface CreatableEntity {

    LocalDateTime getCreatedTime();

    void setCreatedTime(LocalDateTime createdTime);

}
