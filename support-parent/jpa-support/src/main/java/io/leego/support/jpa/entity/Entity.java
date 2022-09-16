package io.leego.support.jpa.entity;

import org.springframework.data.domain.Persistable;

/**
 * @author Leego Yih
 */
public interface Entity<ID> extends Persistable<ID> {

    ID getId();

}
