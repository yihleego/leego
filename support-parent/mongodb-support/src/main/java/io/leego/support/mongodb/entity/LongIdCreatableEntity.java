package io.leego.support.mongodb.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class LongIdCreatableEntity extends CreatableEntity<Long> {
    @Override
    @MongoId(FieldType.INT64)
    public Long getId() {
        return id;
    }
}
