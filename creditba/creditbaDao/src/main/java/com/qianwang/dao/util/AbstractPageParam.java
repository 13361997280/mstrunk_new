package com.qianwang.dao.util;

/**
 * Created by yanglikai on 2017/4/18.
 */
public abstract class AbstractPageParam {
    public static final String DESC = "desc";
    public static final String ASC = "asc";
    public static final String id = "id";

    protected int page = 1;
    protected int rows = 20;
    protected String order = DESC;
    protected String sort = id;
    protected int start;

    protected int resetStart;

    public AbstractPageParam() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1) {
            this.page = 1;
        } else {
            this.page = page;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getStart() {
        return start = (page - 1) * rows;
    }

    public void setResetStart(Integer resetStart) {
        this.resetStart = resetStart;
    }

    public int getResetStart() {
        return resetStart;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
