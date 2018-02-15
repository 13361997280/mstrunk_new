package com.qianwang.mapper.user;

import java.util.List;
import java.util.Map;

import com.qianwang.dao.domain.user.LightUserChannelActionStatDay;
import com.qianwang.dao.domain.user.LightUserChannelActionStatDayKey;

public interface LightUserChannelActionStatDayMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table light_user_channel_action_stat_day
     *
     * @mbggenerated Tue Nov 25 14:11:51 CST 2014
     */
    int deleteByPrimaryKey(LightUserChannelActionStatDayKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table light_user_channel_action_stat_day
     *
     * @mbggenerated Tue Nov 25 14:11:51 CST 2014
     */
    int insert(LightUserChannelActionStatDay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table light_user_channel_action_stat_day
     *
     * @mbggenerated Tue Nov 25 14:11:51 CST 2014
     */
    int insertSelective(LightUserChannelActionStatDay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table light_user_channel_action_stat_day
     *
     * @mbggenerated Tue Nov 25 14:11:51 CST 2014
     */
    LightUserChannelActionStatDay selectByPrimaryKey(LightUserChannelActionStatDayKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table light_user_channel_action_stat_day
     *
     * @mbggenerated Tue Nov 25 14:11:51 CST 2014
     */
    int updateByPrimaryKeySelective(LightUserChannelActionStatDay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table light_user_channel_action_stat_day
     *
     * @mbggenerated Tue Nov 25 14:11:51 CST 2014
     */
    int updateByPrimaryKey(LightUserChannelActionStatDay record);
    
    List<Map<String, Object>>getUserSource(Map<String, Object>params);
    List<Map<String, Object>>getUserSourceHour(Map<String, Object>params);
    List<Map<String, Object>>getUserSourceGroup(Map<String, Object>params);
    List<Map<String, Object>>getUserSourceHourGroup(Map<String, Object>params);

	List<Map<String, Object>> getSignInfo(Map<String, Object> params);

	List<Map<String, Object>> getClassifySignInfo(Map<String, Object> params);

	List<Map<String, Object>> getSignHourInfo(Map<String, Object> params);

	List<Map<String, Object>> getClassifySignHourInfo(Map<String, Object> params);

	List<Map<String, Object>> getUserRechargeDist(Map<String, Object> params);

    List<Map<String, Object>> getTodayRegisterInfo(Map<String, Object> params);

    List<Map<String, Object>> getLastSixWeekRegisterInfo(Map<String, Object> params);

	int depositAlertCnt(Map<String, Object> params);

	List<Map<String, Object>> getUserSourceRec(Map<String, Object> params);

	List<Map<String, Object>> getUserSourceRecHour(Map<String, Object> params);

	List<Map<String, Object>> getAllUserTaskActivity(Map<String, Object> params);

	List<Map<String, Object>> getAllUserTaskActivityCustom(
			Map<String, Object> params);

	List<Map<String, Object>> getUserTaskActivityTblInfo(
			Map<String, Object> params);

    List<Map<String,Object>> findUserInfoByMobile(Map<String, Object> params);


    List<Map<String,Object>> findLastRegistUserRecent();
}