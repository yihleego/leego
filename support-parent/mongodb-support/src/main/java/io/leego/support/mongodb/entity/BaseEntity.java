package io.leego.support.mongodb.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
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
public abstract class BaseEntity<ID extends Serializable> {
    public static final Set<String> IGNORED = Set.of("class", "id", "createdTime", "updatedTime", "deleted", "deletedTime");
    protected ID id;

    public <T> void merge(T source) {
        Object target = this;
        if (!target.getClass().isInstance(source)) {
            throw new IllegalArgumentException();
        }
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(target.getClass());
        try {
            for (PropertyDescriptor pd : pds) {
                if (IGNORED.contains(pd.getName())) {
                    continue;
                }
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    Object value = readMethod.invoke(source);
                    if (value != null) {
                        Method writeMethod = pd.getWriteMethod();
                        if (writeMethod != null) {
                            writeMethod.invoke(target, value);
                        }
                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new FatalBeanException("Could not merge", e);
        }
    }
}
