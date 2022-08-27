package io.leego.support.mongodb.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
public abstract class UpdatableEntity<ID extends Serializable> extends BaseEntity<ID> implements Creatable<ID>, Updatable<ID> {
    @CreatedDate
    protected LocalDateTime createdTime;
    @LastModifiedDate
    protected LocalDateTime updatedTime;
}
