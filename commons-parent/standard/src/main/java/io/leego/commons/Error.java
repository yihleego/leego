package io.leego.commons;

/**
 * @author Leego Yih
 */
public interface Error {

    /** Returns error code. */
    Integer getCode();

    /** Returns error message. */
    String getMessage();

}