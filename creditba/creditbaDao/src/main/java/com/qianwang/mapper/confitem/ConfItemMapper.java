package com.qianwang.mapper.confitem;


import com.qianwang.dao.domain.confitem.ConfItem;
import com.qianwang.param.confitem.ItemPageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfItemMapper  {

    ConfItem selectByPrimaryKey(@Param("id") Integer id);

    List<ConfItem> selectAll(ItemPageParam pageparam);

    int  selectAllCount();

    int insertSelective(ConfItem confItem);

    int updateByPrimaryKeySelective(ConfItem confItem);
}