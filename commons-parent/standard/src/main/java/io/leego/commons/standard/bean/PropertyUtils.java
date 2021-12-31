package io.leego.commons.standard.bean;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 */
public final class PropertyUtils {
    private PropertyUtils() {
    }

    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(9);

    /**
     * Map with primitive type as key and corresponding wrapper
     * type as value, for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(9);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
        primitiveWrapperTypeMap.put(Void.class, void.class);

        // Map entry iteration is less expensive to initialize than forEach with lambdas
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static void copy(Object source, Object target) throws PropertyAccessException {
        copy(source, target, null, false, (String[]) null);
    }

    public static void copy(Object source, Object target, Class<?> editable) throws PropertyAccessException {
        copy(source, target, editable, false, (String[]) null);
    }

    public static void copy(Object source, Object target, String... ignoreProperties) throws PropertyAccessException {
        copy(source, target, null, false, ignoreProperties);
    }

    public static void copy(Object source, Object target, boolean ignoreNull) throws PropertyAccessException {
        copy(source, target, null, ignoreNull, (String[]) null);
    }

    public static void copy(Object source, Object target, Class<?> editable, boolean ignoreNull) throws PropertyAccessException {
        copy(source, target, editable, ignoreNull, (String[]) null);
    }

    public static void copy(Object source, Object target, boolean ignoreNull, String... ignoreProperties) throws PropertyAccessException {
        copy(source, target, null, ignoreNull, ignoreProperties);
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>As of Spring Framework 5.3, this method honors generic type information
     * when matching properties in the source and target objects.
     * @param source           the source bean
     * @param target           the target bean
     * @param editable         the class (or interface) to restrict property setting to
     * @param ignoreNull       whether to ignore the property values
     * @param ignoreProperties array of property names to ignore
     * @throws PropertyAccessException if the copying failed
     */
    private static void copy(Object source, Object target, Class<?> editable, boolean ignoreNull, String... ignoreProperties) throws PropertyAccessException {
        if (source == null || target == null) {
            return;
        }
        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
        try {
            PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
            Map<String, PropertyDescriptor> sourcePdMap = sourcePds.length > 0
                    ? Arrays.stream(sourcePds).collect(Collectors.toMap(FeatureDescriptor::getName, Function.identity()))
                    : Collections.emptyMap();
            for (PropertyDescriptor targetPd : targetPds) {
                if (targetPd.getWriteMethod() == null || (ignoreList != null && ignoreList.contains(targetPd.getName()))) {
                    continue;
                }
                PropertyDescriptor sourcePd = sourcePdMap.get(targetPd.getName());
                if (sourcePd == null || sourcePd.getReadMethod() == null) {
                    continue;
                }
                Method readMethod = sourcePd.getReadMethod();
                Method writeMethod = targetPd.getWriteMethod();
                if (isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    if (value == null && ignoreNull) {
                        continue;
                    }
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(target, value);
                }
            }
        } catch (Throwable e) {
            throw new PropertyAccessException(e);
        }
    }

    /**
     * Returns descriptors for all properties of the bean.
     * @param beanClass The bean class to be analyzed
     * @return an array of {@code PropertyDescriptor} objects
     * @throws IntrospectionException if an exception occurs during introspection.
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) throws IntrospectionException {
        return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
    }

    /**
     * Check if the right-hand side type may be assigned to the left-hand side
     * type, assuming setting by reflection. Considers primitive wrapper
     * classes as assignable to the corresponding primitive types.
     * @param lhsType the target type
     * @param rhsType the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     */
    private static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            return (lhsType == resolvedPrimitive);
        } else {
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            return (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper));
        }
    }
}
