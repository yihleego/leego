package io.leego.support.mongodb.entity;

import java.time.Instant;

/**
 * @author Leego Yih
 */
public interface Creatable {

    Instant getCreatedTime();

    void setCreatedTime(Instant createdTime);

}
