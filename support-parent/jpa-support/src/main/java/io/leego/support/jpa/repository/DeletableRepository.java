package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA soft-deleting specific extension of {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author Leego Yih
 */
@NoRepositoryBean
public interface DeletableRepository<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID> {

    /**
     * Marks the entity as deleted with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} set deleted = id, deletedTime = :#{T(java.time.LocalDateTime).now()} where id = :id and deleted = 0")
    void deleteById(@Param("id") ID id);

    /**
     * Marks all entities as deleted with the given ids.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     */
    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} set deleted = id, deletedTime = :#{T(java.time.LocalDateTime).now()} where id in :ids and deleted = 0")
    void deleteAllById(@Param("ids") Iterable<? extends ID> ids);

    /**
     * Marks all entities managed by the repository as deleted.
     */
    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} set deleted = id, deletedTime = :#{T(java.time.LocalDateTime).now()} where deleted = 0")
    void deleteAll();

    /**
     * Marks the given entity as deleted.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    @Transactional
    default void delete(T entity) {
        if (entity.getId() != null) {
            this.deleteById(entity.getId());
        }
    }

    /**
     * Marks the given entities as deleted.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        List<ID> ids = new ArrayList<>();
        for (T entity : entities) {
            if (entity.getId() != null) {
                ids.add(entity.getId());
            }
        }
        if (!ids.isEmpty()) {
            this.deleteAllById(ids);
        }
    }

    /**
     * Marks the given entities as deleted in a batch which means it will create a single query.
     *
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    @Transactional
    default void deleteAllInBatch(Iterable<T> entities) {
        this.deleteAll(entities);
    }

    /**
     * Marks the entities identified by the given ids as deleted using a single query.
     *
     * @param ids the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    @Transactional
    default void deleteAllByIdInBatch(Iterable<ID> ids) {
        this.deleteAllById(ids);
    }

    /**
     * Marks all entities as deleted in a batch call.
     */
    @Override
    @Transactional
    default void deleteAllInBatch() {
        this.deleteAll();
    }

}
