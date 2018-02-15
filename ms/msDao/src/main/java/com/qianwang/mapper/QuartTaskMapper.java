package com.qianwang.mapper;

import com.qianwang.dao.domain.QuartTask;

import java.util.List;

public interface QuartTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuartTask record);

    int insertSelective(QuartTask record);

    QuartTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuartTask record);

    int updateByPrimaryKey(QuartTask record);

    List<QuartTask> selectAll();
}