package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author Leego Yih
 */
@NoRepositoryBean
public interface CrudRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends CreatableRepository<T, ID>, UpdatableRepository<T, ID>, DeletableRepository<T, ID> {
}
