package com.qianwang.mapper;

import com.qianwang.dao.domain.ActSend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActSendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActSend record);

    int insertSelective(ActSend record);

    ActSend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActSend record);

    int updateByPrimaryKey(ActSend record);

    List<ActSend> selectByPage(@Param("index") int index, @Param("size") int size, @Param("userId") int userId);

    int selectByPageCount(int userId);
}