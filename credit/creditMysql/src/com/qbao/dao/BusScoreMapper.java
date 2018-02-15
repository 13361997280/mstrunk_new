package com.qbao.dao;

import com.qbao.dto.BusScore;

public interface BusScoreMapper {
    int insertSelective(BusScore record);
    BusScore selectByUserId(Integer userId);
    int updateByUserIdSelective(BusScore record);
}