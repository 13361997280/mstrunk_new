package com.qianwang.mapper.assistant;

import com.qianwang.dao.domain.assistant.AssUserPRecommendationFrozen;

import java.util.List;

public interface AssUserPRecommendationFrozenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AssUserPRecommendationFrozen record);

    int insertSelective(AssUserPRecommendationFrozen record);

    AssUserPRecommendationFrozen selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AssUserPRecommendationFrozen record);

    int updateByPrimaryKey(AssUserPRecommendationFrozen record);

    List<AssUserPRecommendationFrozen> selectReviewData();
}