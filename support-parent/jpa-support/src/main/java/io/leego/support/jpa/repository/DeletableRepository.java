package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leego Yih
 */
@NoRepositoryBean
public interface DeletableRepository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} set deleted = id, deletedTime = :#{T(java.time.LocalDateTime).now()} where id = :id and deleted = 0")
    void deleteById(@Param("id") ID id);

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} set deleted = id, deletedTime = :#{T(java.time.LocalDateTime).now()} where id in :ids and deleted = 0")
    void deleteAllById(@Param("ids") Iterable<? extends ID> ids);

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} set deleted = id, deletedTime = :#{T(java.time.LocalDateTime).now()} where deleted = 0")
    void deleteAll();

    @Override
    @Transactional
    default void delete(T entity) {
        if (entity.getId() != null) {
            this.deleteById(entity.getId());
        }
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        List<ID> ids = new ArrayList<>();
        for (T entity : entities) {
            if (entity.getId() != null) {
                ids.add(entity.getId());
            }
        }
        this.deleteAllById(ids);
    }

    @Override
    @Transactional
    default void deleteAllInBatch(Iterable<T> entities) {
        this.deleteAll(entities);
    }

    @Override
    @Transactional
    default void deleteAllByIdInBatch(Iterable<ID> ids) {
        this.deleteAllById(ids);
    }

    @Override
    @Transactional
    default void deleteAllInBatch() {
        this.deleteAll();
    }

}
