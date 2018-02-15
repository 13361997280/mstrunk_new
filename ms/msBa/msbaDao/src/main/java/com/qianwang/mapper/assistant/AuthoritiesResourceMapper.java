package com.qianwang.mapper.assistant;

import com.qianwang.dao.domain.assistant.AuthoritiesResource;

import java.util.List;

public interface AuthoritiesResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthoritiesResource record);

    int insertSelective(AuthoritiesResource record);

    AuthoritiesResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthoritiesResource record);

    int updateByPrimaryKey(AuthoritiesResource record);
    int selectCount();
    List<AuthoritiesResource> selectList();
}