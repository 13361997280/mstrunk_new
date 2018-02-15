package com.qianwang.service.vo.confitem;

import com.qianwang.dao.domain.confitem.ConfItem;

import java.util.List;

/**
 * Created by fun
 * on 2017/11/18.
 */
public class ItemQueryAllVO {

    private int count;

    private List<ConfItem> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ConfItem> getItems() {
        return items;
    }

    public void setItems(List<ConfItem> items) {
        this.items = items;
    }
}
