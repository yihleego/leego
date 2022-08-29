package io.leego.support.jpa.repository;

import io.leego.support.jpa.entity.BaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 */
@NoRepositoryBean
public interface CreatableRepository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

    @Transactional
    default <S> T save(S source, T entity) {
        if (source != null) {
            BeanUtils.copyProperties(source, entity);
        }
        return this.save(entity);
    }

    @Transactional
    default <S> T save(S source, T entity, BiFunction<S, T, T> mapper) {
        if (source != null) {
            BeanUtils.copyProperties(source, entity);
        }
        return this.save(mapper.apply(source, entity));
    }

    @Transactional
    default <S> T save(S source, Supplier<T> factory) {
        return this.save(source, factory.get());
    }

    @Transactional
    default <S> T save(S source, Supplier<T> factory, BiFunction<S, T, T> mapper) {
        return this.save(source, factory.get(), mapper);
    }

    @Transactional
    default <S> List<T> saveAll(Collection<S> sources, Supplier<T> factory) {
        List<T> entities = sources.stream()
                .map(source -> {
                    T entity = factory.get();
                    BeanUtils.copyProperties(source, entity);
                    return entity;
                })
                .collect(Collectors.toList());
        return this.saveAll(entities);
    }

    @Transactional
    default <S> List<T> saveAll(Collection<S> sources, Supplier<T> factory, BiFunction<S, T, T> mapper) {
        List<T> entities = sources.stream()
                .map(source -> {
                    T entity = factory.get();
                    BeanUtils.copyProperties(source, entity);
                    return mapper.apply(source, entity);
                })
                .collect(Collectors.toList());
        return this.saveAll(entities);
    }

}
