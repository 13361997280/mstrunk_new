package com.qianwang.mapper.assistant;

import com.qianwang.dao.domain.assistant.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    List<Users> selectList(@Param("start") Integer start, @Param("size") Integer size);

    List<Users> selectListForAuth(@Param("id") Integer id);

    int selectCount();

    int existUserName(String username);
}