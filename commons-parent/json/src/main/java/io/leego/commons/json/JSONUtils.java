package io.leego.commons.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Leego Yih
 */
public final class JSONUtils {
    private static final ObjectMapper mapper;
    private static final ObjectMapper nonNullMapper;

    static {
        mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        nonNullMapper = mapper.copy()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JSONUtils() {
    }

    public static ObjectMapper getMapper() {
        return mapper.copy();
    }

    public static ObjectMapper getNonNullMapper() {
        return nonNullMapper.copy();
    }

    public static String toJSONString(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JSONException(e);
        }
    }

    public static byte[] toJSONBytes(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new JSONException(e);
        }
    }

    public static String toJSONString(Object value, boolean includeNonNull) {
        if (value == null) {
            return null;
        }
        try {
            return includeNonNull
                    ? nonNullMapper.writeValueAsString(value)
                    : mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JSONException(e);
        }
    }

    public static byte[] toJSONBytes(Object value, boolean includeNonNull) {
        if (value == null) {
            return null;
        }
        try {
            return includeNonNull
                    ? nonNullMapper.writeValueAsBytes(value)
                    : mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(String json, Class<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(String json, JavaType type) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(String json, TypeReference<T> ref) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, ref);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(byte[] bytes, Class<T> type) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(byte[] bytes, JavaType type) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(byte[] bytes, TypeReference<T> ref) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, ref);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(InputStream inputStream, Class<T> type) {
        if (inputStream == null) {
            return null;
        }
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(InputStream inputStream, JavaType type) {
        if (inputStream == null) {
            return null;
        }
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parse(InputStream inputStream, TypeReference<T> ref) {
        if (inputStream == null) {
            return null;
        }
        try {
            return mapper.readValue(inputStream, ref);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <E> List<E> parseList(String json, Class<E> type) {
        if (json == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, type);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <E> List<E> parseList(byte[] bytes, Class<E> type) {
        if (bytes == null) {
            return Collections.emptyList();
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, type);
            return mapper.readValue(bytes, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <E> List<E> parseList(InputStream inputStream, Class<E> type) {
        if (inputStream == null) {
            return Collections.emptyList();
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, type);
            return mapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <E> Set<E> parseSet(String json, Class<E> type) {
        if (json == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Set.class, type);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <E> Set<E> parseSet(byte[] bytes, Class<E> type) {
        if (bytes == null) {
            return Collections.emptySet();
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Set.class, type);
            return mapper.readValue(bytes, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <E> Set<E> parseSet(InputStream inputStream, Class<E> type) {
        if (inputStream == null) {
            return Collections.emptySet();
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Set.class, type);
            return mapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyType, Class<V> valueType) {
        if (json == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <K, V> Map<K, V> parseMap(byte[] bytes, Class<K> keyType, Class<V> valueType) {
        if (bytes == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType);
            return mapper.readValue(bytes, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <K, V> Map<K, V> parseMap(InputStream inputStream, Class<K> keyType, Class<V> valueType) {
        if (inputStream == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType);
            return mapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public static <T> T convert(Object source, Class<T> type) throws IllegalArgumentException {
        return mapper.convertValue(source, type);
    }

    public static <T> T convert(Object source, JavaType type) throws IllegalArgumentException {
        return mapper.convertValue(source, type);
    }

    public static <T> T convert(Object source, TypeReference<T> ref) throws IllegalArgumentException {
        return mapper.convertValue(source, ref);
    }

    public static JavaType toJavaType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Class<?> rowClass = (Class<?>) ((ParameterizedType) type).getRawType();
            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                javaTypes[i] = toJavaType(actualTypeArguments[i]);
            }
            return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
        } else {
            Class<?> clazz = (Class<?>) type;
            return TypeFactory.defaultInstance().constructParametricType(clazz, new JavaType[0]);
        }
    }

    public static JavaType toJavaType(Class<?> rawType) {
        return SimpleType.constructUnsafe(rawType);
    }

    public static JavaType toJavaType(Class<?> rawType, JavaType parameterType) {
        return TypeFactory.defaultInstance().constructParametricType(rawType, parameterType);
    }

    public static JavaType toJavaType(Class<?> rawType, JavaType... parameterTypes) {
        return TypeFactory.defaultInstance().constructParametricType(rawType, parameterTypes);
    }
}
