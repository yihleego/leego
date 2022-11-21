package io.leego.support.mongodb.entity;

import java.time.Instant;

/**
 * @author Leego Yih
 */
public interface Updatable {

    Instant getUpdatedTime();

    void setUpdatedTime(Instant updatedTime);

}
