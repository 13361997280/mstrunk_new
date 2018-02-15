package com.qianwang.mapper.confbaseset;

import com.qianwang.dao.domain.confbaseset.ConfBaseSet;
import com.qianwang.param.confbaseset.BaseSetParam;

import java.util.List;


public interface ConfBaseSetMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ConfBaseSet record);

    int insertSelective(ConfBaseSet record);

    ConfBaseSet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfBaseSet record) ;

    int updateByPrimaryKey(ConfBaseSet record);

    ConfBaseSet selectByOnline();

    List<ConfBaseSet> selectByOffline(BaseSetParam baseSetParam);

    int selectByOfflineCount();

}