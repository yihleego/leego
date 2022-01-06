package io.leego.commons.standard;

import java.io.Serializable;

/**
 * @author Leego Yih
 */
public class PageParam implements Pageable, Serializable {
    private static final long serialVersionUID = -3767519710332901335L;
    /** One-based page index. */
    protected Integer page;
    /** The size of the page to be returned. */
    protected Integer size;
    /** Zero-based row index. */
    protected Long offset;
    /** The rows of the page to be returned. */
    protected Integer rows;

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
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    @Override
    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
