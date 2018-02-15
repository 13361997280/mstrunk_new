package com.qianwang.mapper.assistant;

import com.qianwang.dao.domain.assistant.Authorities;

import java.util.List;

public interface AuthoritiesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Authorities record);

    int insertSelective(Authorities record);

    Authorities selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Authorities record);

    int updateByPrimaryKey(Authorities record);
    int selectAuthoritiesCount();
    List<Authorities> selectAuthorities();
}