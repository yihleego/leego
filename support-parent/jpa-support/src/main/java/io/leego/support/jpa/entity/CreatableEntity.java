package io.leego.support.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldNameConstants
@MappedSuperclass
public abstract class CreatableEntity<ID> extends BaseEntity<ID> implements Creatable {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Instant createdTime;
}
