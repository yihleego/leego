package io.leego.commons.standard;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Leego Yih
 */
public class PageRequest extends SortRequest implements Pageable, Sortable, Serializable {
    @Serial
    private static final long serialVersionUID = -542101357510265940L;
    /** One-based page index. */
    protected Integer page;
    /** The size of the page to be returned. */
    protected Integer size;

    public PageRequest() {
    }

    public PageRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public PageRequest(Integer page, Integer size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    @Override
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public Long getOffset() {
        if (page != null && size != null) {
            return (long) (page - 1) * (long) size;
        } else {
            return null;
        }
    }

    @Override
    public PageRequest next() {
        return new PageRequest(getPage() + 1, getSize(), getSort());
    }

    @Override
    public PageRequest previous() {
        return getPage() == 0 ? this : new PageRequest(getPage() - 1, getSize(), getSort());
    }

    @Override
    public PageRequest first() {
        return new PageRequest(0, getSize(), getSort());
    }
}
