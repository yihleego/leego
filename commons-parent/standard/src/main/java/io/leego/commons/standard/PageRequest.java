package io.leego.commons.standard;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Leego Yih
 */
public class PageRequest implements Pageable, Serializable {
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
}
