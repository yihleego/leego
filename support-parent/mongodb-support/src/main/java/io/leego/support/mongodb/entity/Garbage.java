package io.leego.support.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("garbage")
public class Garbage {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String type;
    private Object data;
    @Indexed
    @Field(targetType = FieldType.TIMESTAMP)
    private Instant collectedTime;

    public Garbage(String type, Object data, Instant collectedTime) {
        this.type = type;
        this.data = data;
        this.collectedTime = collectedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity<?> that)) return false;
        return this.id != null && Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}