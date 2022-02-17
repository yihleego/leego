package io.leego.commons.standard;

/**
 * @author Leego Yih
 */
public interface Error {

    /** Returns error code. */
    Integer getCode();

    /** Returns error message. */
    String getMessage();

}