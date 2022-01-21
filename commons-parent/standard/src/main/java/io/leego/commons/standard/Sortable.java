package io.leego.commons.standard;

/**
 * @author Leego Yih
 */
public interface Sortable {

    /** Returns the sorting parameters. */
    Sort getSort();

    /** Returns whether the current {@link Sortable} contains sorting information. */
    boolean isSorted();

}
