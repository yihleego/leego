package io.leego.support.jpa.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@FieldNameConstants
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Persistable<ID> {
    public static final Set<String> IGNORED = Set.of("class", "new", "_new", "id", "createdTime", "updatedTime", "deleted", "deletedTime");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected ID id;
    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient boolean _new = true;

    /**
     * Persist the entity if the returned value is <code>true</code>,
     * otherwise merge the entity.
     *
     * @see jakarta.persistence.EntityManager#persist(Object)
     * @see jakarta.persistence.EntityManager#merge(Object)
     */
    @Override
    public boolean isNew() {
        return _new;
    }

    @PostLoad
    void postLoad() {
        this._new = false;
    }

    public void makeNew() {
        this._new = true;
    }

    public void makeNotNew() {
        this._new = false;
    }

    public <T> void merge(T source, String... ignoreProperties) {
        // It is guaranteed that the class of the source equals or inherits this class
        Object target = this;
        if (!target.getClass().isInstance(source)) {
            throw new IllegalArgumentException();
        }
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor pd : pds) {
            if (IGNORED.contains(pd.getName()) || ObjectUtils.containsElement(ignoreProperties, pd.getName())) {
                continue;
            }
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod != null) {
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    try {
                        // Only non-null properties will be merged
                        Object value = readMethod.invoke(source);
                        if (value != null) {
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable e) {
                        throw new FatalBeanException("Could not merge property '" + pd.getName() + "'", e);
                    }
                }
            }
        }
    }
}
