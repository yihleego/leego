package io.leego.commons.standard;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 */
public interface Enum<T> {

    T getCode();

    static <T, E extends Enum<T>> Map<T, E> toMap(Class<E> clazz) {
        return Arrays.stream(clazz.getEnumConstants())
                .collect(Collectors.toUnmodifiableMap(Enum::getCode, Function.identity()));
    }

    static <T, E extends Enum<T>> E get(Class<E> clazz, T code) {
        return get(clazz, code, null);
    }

    static <T, E extends Enum<T>> E get(Class<E> clazz, T code, E defaultValue) {
        return Arrays.stream(clazz.getEnumConstants())
                .filter(o -> o.getCode() == code)
                .findAny()
                .orElse(defaultValue);
    }

}
