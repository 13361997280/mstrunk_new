package com.qianwang.mapper.confratio;

import com.qianwang.dao.domain.advertisement.ConfRatio;

import java.util.List;

public interface ConfRatioMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfRatio record);

    int insertSelective(ConfRatio record);

    ConfRatio selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfRatio record);

    int updateByPrimaryKey(ConfRatio record);

    List<ConfRatio> getAll();


    List<ConfRatio> getStatus(List<String> list);

    void updateStatus();

    List<String> getDis();

    List<ConfRatio> getListBylistName(ConfRatio record);
}