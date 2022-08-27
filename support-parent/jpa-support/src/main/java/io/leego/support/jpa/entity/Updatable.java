package io.leego.support.jpa.entity;

import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Updatable<ID> extends Persistable<ID> {

    LocalDateTime getUpdatedTime();

    void setUpdatedTime(LocalDateTime updatedTime);

}
