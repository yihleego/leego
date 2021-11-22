package io.leego.commons.standard.entity;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface UpdatableEntity {

    LocalDateTime getUpdatedTime();

    void setUpdatedTime(LocalDateTime updatedTime);

}
