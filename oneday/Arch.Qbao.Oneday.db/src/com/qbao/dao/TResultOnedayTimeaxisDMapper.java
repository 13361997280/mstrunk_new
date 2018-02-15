package com.qbao.dao;

import com.qbao.dto.TResultOnedayTimeaxisD;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TResultOnedayTimeaxisDMapper {
    List<TResultOnedayTimeaxisD> select(@Param("start") int start, @Param("size") int size);

    int insertBatch(List<TResultOnedayTimeaxisD> list);
}