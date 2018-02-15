package com.qianwang.mapper;

import com.qianwang.dao.domain.UserSelectGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserSelectGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSelectGroup record);

    int insertSelective(UserSelectGroup record);

    UserSelectGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSelectGroup record);

    int updateByPrimaryKey(UserSelectGroup record);

    List<UserSelectGroup> selectAll(Integer userId);

    List<UserSelectGroup> selectByPage(@Param("index") int index, @Param("size") int size, @Param("userId") int userId);

    int selectByPageCount(int userId);

    UserSelectGroup selectByUserSelectId(String selectId);
}