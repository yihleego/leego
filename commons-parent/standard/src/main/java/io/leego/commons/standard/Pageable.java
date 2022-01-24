package io.leego.commons.standard;

/**
 * @author Leego Yih
 */
public interface Pageable extends Sortable {

    /** Returns the page to be returned, zero-based page index, must be greater than zero. */
    Integer getPage();

    /** Returns the size of the page to be returned, must be greater than zero. */
    Integer getSize();

    /** Returns the offset to be taken according to the underlying page and size. */
    Long getOffset();

    /** Returns the {@link Pageable} requesting the next {@link Page}. */
    Pageable next();

    /** Returns the previous {@link Pageable} or the first {@link Pageable} if the current one already is the first one. */
    Pageable previous();

    /** Returns the {@link Pageable} requesting the first page. */
    Pageable first();

    /** Returns whether the current {@link Pageable} contains pagination information. */
    default boolean isPaged() {
        return isValid(getPage(), getSize());
    }

    /** Returns <code>true</code> if the <code>page</code> and <code>size</code> are valid. */
    static boolean isValid(Integer page, Integer size) {
        return page != null && size != null && page > 0 && size > 0;
    }
}
