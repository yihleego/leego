package io.leego.commons.standard;

/**
 * @author Leego Yih
 */
public interface Pageable {

    /** Returns one-based page index. */
    Integer getPage();

    /** Returns the size of the page to be returned. */
    Integer getSize();

}
