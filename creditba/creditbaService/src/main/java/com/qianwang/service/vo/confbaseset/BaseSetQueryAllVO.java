package com.qianwang.service.vo.confbaseset;

import com.qianwang.dao.domain.confbaseset.ConfBaseSet;

import java.util.List;

/**
 * Created by fun
 * on 2017/11/18.
 */
public class BaseSetQueryAllVO {

    private ConfBaseSet confBaseSet;
    private List<ConfBaseSet> items;
    private int count;

    public ConfBaseSet getConfBaseSet() {
        return confBaseSet;
    }

    public void setConfBaseSet(ConfBaseSet confBaseSet) {
        this.confBaseSet = confBaseSet;
    }

    public List<ConfBaseSet> getItems() {
        return items;
    }

    public void setItems(List<ConfBaseSet> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
