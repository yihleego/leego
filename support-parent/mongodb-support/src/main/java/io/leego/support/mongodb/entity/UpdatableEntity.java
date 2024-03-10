package io.leego.support.mongodb.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldNameConstants
public abstract class UpdatableEntity<ID> extends BaseEntity<ID> implements Creatable, Updatable {
    @CreatedDate
    @Field(targetType = FieldType.TIMESTAMP)
    protected Instant createdTime;
    @LastModifiedDate
    @Field(targetType = FieldType.TIMESTAMP)
    protected Instant updatedTime;
}
