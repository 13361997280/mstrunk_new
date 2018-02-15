package com.qianwang.mapper;

import com.qianwang.dao.domain.AuthoritiesGroup;

public interface AuthoritiesGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthoritiesGroup record);

    int insertSelective(AuthoritiesGroup record);

    AuthoritiesGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthoritiesGroup record);

    int updateByPrimaryKey(AuthoritiesGroup record);

    String selectResouceIdByUserId(Integer userId);
}