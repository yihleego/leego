package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author Leego Yih
 */
@NoRepositoryBean
public interface UnionRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID>, CreatableRepository<T, ID>, UpdatableRepository<T, ID>, DeletableRepository<T, ID> {
}
