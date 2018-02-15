package com.qianwang.service.confbaseset;

import com.qianwang.dao.domain.confbaseset.ConfBaseSet;

import com.qianwang.service.dto.confbaseset.BaseSetPageDto;

import com.qianwang.service.vo.confbaseset.BaseSetQueryAllVO;


/**
 * Created by fun
 * on 2017/11/17.
 */
public interface ConfBaseSetService {

     BaseSetQueryAllVO  selectAll(BaseSetPageDto dto );

    int  save(ConfBaseSet confBaseSet);
}
