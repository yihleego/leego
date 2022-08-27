package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leego Yih
 */
@NoRepositoryBean
public interface UpdatableRepository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

    @Nullable
    @Transactional
    default T update(T entity) {
        return this.update(entity, (String[]) null);
    }

    @Nullable
    @Transactional
    default T update(T entity, String... ignoreProperties) {
        T o = this.getReferenceById(Objects.requireNonNull(entity.getId()));
        try {
            o.merge(entity, ignoreProperties);
            return this.saveAndFlush(o);
        } catch (EntityNotFoundException ignored) {
            return null;
        }
    }

}
