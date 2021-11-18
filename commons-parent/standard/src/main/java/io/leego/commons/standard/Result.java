package io.leego.commons.standard;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Leego Yih
 */
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -6923820166518806231L;
    private T data;
    private Boolean success;
    private String message;
    private Integer code;

    public Result() {
    }

    public Result(T data, Boolean success, String message, Integer code) {
        this.data = data;
        this.success = success;
        this.message = message;
        this.code = code;
    }

    protected Result(Boolean success) {
        this.success = success;
    }

    public static <T> Result<T> buildSuccess(Integer code, String message, T data) {
        return new Result<>(data, true, message, code);
    }

    public static <T> Result<T> buildSuccess(Integer code, T data) {
        return new Result<>(data, true, null, code);
    }

    public static <T> Result<T> buildSuccess(String message, T data) {
        return new Result<>(data, true, message, null);
    }

    public static <T> Result<T> buildSuccess(String message) {
        return new Result<>(null, true, message, null);
    }

    public static <T> Result<T> buildSuccess(T data) {
        return new Result<>(data, true, null, null);
    }

    public static <T> Result<T> buildSuccess() {
        return new Result<>(null, true, null, null);
    }

    public static <T> Result<T> buildFailure(Integer code, String message, T data) {
        return new Result<>(data, false, message, code);
    }

    public static <T> Result<T> buildFailure(Integer code, String message) {
        return new Result<>(null, false, message, code);
    }

    public static <T> Result<T> buildFailure(Integer code) {
        return new Result<>(null, false, null, code);
    }

    public static <T> Result<T> buildFailure(String message, T data) {
        return new Result<>(data, false, message, null);
    }

    public static <T> Result<T> buildFailure(String message) {
        return new Result<>(null, false, message, null);
    }

    public static <T> Result<T> buildFailure() {
        return new Result<>(null, false, null, null);
    }

    public static <T> Result<T> buildFailure(Result<?> result) {
        if (result != null) {
            return new Result<>(null, false, result.getMessage(), result.getCode());
        } else {
            return new Result<>(null, false, null, null);
        }
    }

    public static <T> Result<T> buildFailure(Error error) {
        if (error != null) {
            return new Result<>(null, false, error.getMessage(), error.getCode());
        } else {
            return new Result<>(null, false, null, null);
        }
    }

    public static <T> Result<T> buildFailure(Throwable cause) {
        if (cause != null) {
            return new Result<>(null, false, cause.getMessage(), null);
        } else {
            return new Result<>(null, false, null, null);
        }
    }

    public static <E> Result<List<E>> emptyList() {
        return new Result<>(Collections.emptyList(), true, null, null);
    }

    public static <E> Result<Set<E>> emptySet() {
        return new Result<>(Collections.emptySet(), true, null, null);
    }

    public static <K, V> Result<Map<K, V>> emptyMap() {
        return new Result<>(Collections.emptyMap(), true, null, null);
    }

    public static boolean isSuccessful(Result<?> result) {
        return result != null && result.getSuccess() != null && result.getSuccess();
    }

    public static boolean isUnsuccessful(Result<?> result) {
        return !isSuccessful(result);
    }

    public static boolean isSuccessfulWithData(Result<?> result) {
        return isSuccessful(result) && result.getData() != null;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public <U> Result<U> map(Function<T, U> converter) {
        return new Result<>(converter.apply(data), success, message, code);
    }

    public <U> Result<U> mapOptional(Function<Optional<T>, U> converter) {
        return new Result<>(converter.apply(Optional.ofNullable(data)), success, message, code);
    }

    public <U> Result<U> toFailure() {
        return new Result<>(null, false, message, code);
    }

    @Override
    public String toString() {
        return "Result{data=" + (data != null ? data.toString() : "null") +
                ", success=" + success +
                ", message=\"" + message + '\"' +
                ", code=" + code +
                '}';
    }
}