package io.leego.support.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@MappedSuperclass
public abstract class CrudEntity<ID> extends BaseEntity<ID> implements Creatable, Updatable, Deletable<ID> {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdTime;
    @LastModifiedDate
    protected LocalDateTime updatedTime;
    @Column(insertable = false, updatable = false)
    protected ID deleted;
    @Column(insertable = false, updatable = false)
    protected LocalDateTime deletedTime;

    /**
     * Returns <code>true</code> if it has been deleted,
     * that is, when the <code>deleted</code> is equal to the <code>id</code>.
     *
     * @see io.leego.support.jpa.repository.DeletableRepository
     */
    @Override
    public boolean isDeleted() {
        return Objects.equals(getDeleted(), getId());
    }
}
