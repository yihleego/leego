package io.leego.support.jpa.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class DeletableLongIdEntity extends DeletableEntity<Long> {
    @Override
    public Long getId() {
        return id;
    }
}
