package io.leego.commons.standard;

/**
 * @author Leego Yih
 */
public interface Pageable {

    /** Returns one-based page index. */
    Integer getPage();

    /** Returns the size of the page to be returned. */
    Integer getSize();

    /** Returns the {@link Pageable} requesting the next {@link Page}. */
    Pageable next();

    /** Returns the previous {@link Pageable} or the first {@link Pageable} if the current one already is the first one. */
    Pageable previous();

    /** Returns the {@link Pageable} requesting the first page. */
    Pageable first();

    /** Returns the sorting parameters. */
    Sort getSort();

    /** Returns whether the current {@link Pageable} contains pagination information. */
    default boolean isPaged() {
        return isValid(getPage(), getSize());
    }

    /** Returns whether the current {@link Pageable} does not contain pagination information. */
    default boolean isUnpaged() {
        return !isValid(getPage(), getSize());
    }

    /** Returns <code>true</code> if the <code>page</code> and <code>size</code> are valid. */
    static boolean isValid(Integer page, Integer size) {
        return page != null && size != null && page > 0 && size > 0;
    }
}
