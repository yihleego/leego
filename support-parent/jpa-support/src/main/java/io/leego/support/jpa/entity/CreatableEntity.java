package io.leego.support.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@MappedSuperclass
public abstract class CreatableEntity<ID extends Serializable> extends BaseEntity<ID> implements Creatable<ID> {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdTime;
}
