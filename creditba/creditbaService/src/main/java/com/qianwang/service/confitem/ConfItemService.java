package com.qianwang.service.confitem;

import com.qianwang.dao.domain.confitem.ConfItem;
import com.qianwang.service.dto.confitem.ItemPageDto;
import com.qianwang.service.vo.confitem.ItemQueryAllVO;

/**
 * Created by fun
 * on 2017/11/16.
 */
public interface ConfItemService {

    ItemQueryAllVO selectAll(ItemPageDto dto);

    int insertConfItem(ConfItem confItem);

    int updateByPrimaryKey(ConfItem confItem);

    ConfItem queryByid(Integer id);



}
