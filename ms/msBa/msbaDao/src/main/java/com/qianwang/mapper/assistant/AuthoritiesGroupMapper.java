package com.qianwang.mapper.assistant;

import com.qianwang.dao.domain.assistant.AuthoritiesGroup;

import java.util.List;

public interface AuthoritiesGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthoritiesGroup record);

    int insertSelective(AuthoritiesGroup record);

    AuthoritiesGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthoritiesGroup record);

    int updateByPrimaryKey(AuthoritiesGroup record);
    int selectCount();
    List<AuthoritiesGroup> selectList();
}