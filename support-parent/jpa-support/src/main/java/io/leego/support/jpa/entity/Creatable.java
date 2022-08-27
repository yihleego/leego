package io.leego.support.jpa.entity;

import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
public interface Creatable<ID> extends Persistable<ID> {

    LocalDateTime getCreatedTime();

    void setCreatedTime(LocalDateTime createdTime);

}
