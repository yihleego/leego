package io.leego.commons.standard;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Leego Yih
 */
public class SortRequest implements Sortable, Serializable {
    @Serial
    private static final long serialVersionUID = -8453615821595321534L;
    /** The sorting parameters. */
    protected Sort sort;

    public SortRequest() {
    }

    public SortRequest(Sort sort) {
        this.sort = sort;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public boolean isSorted() {
        return sort != null && sort.isSorted();
    }
}
