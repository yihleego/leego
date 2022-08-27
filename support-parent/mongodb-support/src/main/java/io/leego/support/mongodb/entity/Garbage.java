package io.leego.support.mongodb.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@Document("garbage")
public class Garbage {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String type;
    private Object data;
    @Indexed
    private LocalDateTime collectedTime;

    public Garbage() {
    }

    public Garbage(String type, Object data, LocalDateTime collectedTime) {
        this.type = type;
        this.data = data;
        this.collectedTime = collectedTime;
    }
}