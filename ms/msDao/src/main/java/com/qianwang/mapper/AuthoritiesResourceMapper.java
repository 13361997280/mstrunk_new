package com.qianwang.mapper;

import com.qianwang.dao.domain.AuthoritiesResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthoritiesResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthoritiesResource record);

    int insertSelective(AuthoritiesResource record);

    AuthoritiesResource selectByPrimaryKey(Integer id);

    List<AuthoritiesResource> selectByIds(@Param("ids") String[] ids);

    int updateByPrimaryKeySelective(AuthoritiesResource record);

    int updateByPrimaryKey(AuthoritiesResource record);
}