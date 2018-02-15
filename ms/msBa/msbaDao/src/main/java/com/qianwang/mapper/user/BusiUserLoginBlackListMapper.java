package com.qianwang.mapper.user;

import com.qianwang.dao.domain.user.UnusualUser;

import java.util.List;
import java.util.Map;

public interface BusiUserLoginBlackListMapper {
    List<Map<String, Object>> getBlackUser(Map<String, Object> params);

    List<Map<String, Object>> getBlackUserDayGroup(Map<String, Object> params);

    List<Map<String, Object>> getBlackUserHourGroup(Map<String, Object> params);

    List<Map<String, Object>> getBlackUserReason();

    List<Map<String, Object>> getUnBlackUser(Map<String, Object> params);

    List<Map<String, Object>> getUnBlackUserDayGroup(Map<String, Object> params);

    List<Map<String, Object>> getUnBlackUserHourGroup(Map<String, Object> params);

    List<Map<String, Object>> getUnBlackUserByTime(Map<String, Object> params);

    List<Map<String, Object>> getUnBlackUserByDay(Map<String, Object> params);

    List<Map<String, Object>> getYiChangUser(Map<String, Object> params);

    int updateYiChangUser(UnusualUser unusualUser);

}