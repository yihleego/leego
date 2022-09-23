package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * JPA updating specific extension of {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author Leego Yih
 */
@NoRepositoryBean
public interface UpdatableRepository<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID> {

    /**
     * Updates a given entity.
     * Use the returned instance for further operations as the update operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the updated entity; will be {@literal null} if not present.
     * @throws IllegalArgumentException in case the given {@literal entity} or {@literal id} is {@literal null}.
     */
    @Nullable
    @Transactional
    default T update(T entity) {
        return this.update(entity, (String[]) null);
    }

    /**
     * Updates a given entity, ignoring the given properties.
     * Use the returned instance for further operations as the update operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the updated entity; will be {@literal null} if not present.
     * @throws IllegalArgumentException in case the given {@literal entity} or {@literal id} is {@literal null}.
     */
    @Nullable
    @Transactional
    default T update(T entity, String... ignoreProperties) {
        Assert.notNull(entity, "Entity must not be null");
        Assert.notNull(entity.getId(), "Entity must not be null");
        try {
            // Merge the non-null property values of the given entity into the reference
            T o = this.getReferenceById(entity.getId());
            o.merge(entity, ignoreProperties);
            return this.saveAndFlush(o);
        } catch (EntityNotFoundException ignored) {
            return null;
        }
    }

}
