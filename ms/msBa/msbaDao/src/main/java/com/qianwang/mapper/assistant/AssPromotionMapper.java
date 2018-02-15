package com.qianwang.mapper.assistant;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qianwang.dao.domain.assistant.AssPromotion;

public interface AssPromotionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AssPromotion record);

    int insertSelective(AssPromotion record);

    AssPromotion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AssPromotion record);

    int updateByPrimaryKey(AssPromotion record);

    List<AssPromotion> selectPromotion(Map<String, Object> map);

    AssPromotion selectDefaultPromotion();
    
    int selectAssPromotionCount();
    
    List<AssPromotion> selectAssPromotion(@Param("orderBy") String orderBy, @Param("start") Integer start, @Param("length") Integer length);

}