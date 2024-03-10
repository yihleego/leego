package io.leego.support.mongodb.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * @author Leego Yih
 */
@Getter
@Setter
@ToString
@FieldNameConstants
public abstract class BaseEntity<ID> implements Entity<ID> {
    protected ID id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Merge the non-null property values of the given entity into self,
     * ignoring the given properties.
     */
    public <T> void merge(T entity, String... ignoreProperties) {
        // It is guaranteed that the class of the entity equals or inherits this class
        if (!this.getClass().isInstance(entity)) {
            throw new IllegalArgumentException();
        }
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(this.getClass());
        for (PropertyDescriptor pd : pds) {
            if (IGNORED.contains(pd.getName()) || ObjectUtils.containsElement(ignoreProperties, pd.getName())) {
                continue;
            }
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod != null) {
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    try {
                        // Merge non-null property values only
                        Object value = readMethod.invoke(entity);
                        if (value != null) {
                            writeMethod.invoke(this, value);
                        }
                    } catch (Throwable e) {
                        throw new FatalBeanException("Could not merge property '" + pd.getName() + "'", e);
                    }
                }
            }
        }
    }

    public static final Set<String> IGNORED = Set.of("class", "id", "createdTime", "updatedTime");
}
