package com.qbao.dao;

import com.qbao.dto.BusScoreDetail;

public interface BusScoreDetailMapper {
    int insertSelective(BusScoreDetail record);
    BusScoreDetail selectByUserId(Integer userId);
    int updateByUserIdSelective(BusScoreDetail record);
    Double todayScore(BusScoreDetail record);
    boolean isExist(BusScoreDetail record);
}