package com.qianwang.service.user;

import java.util.List;
import java.util.Map;

public interface BlackUserService {
	List<Map<String, Object>> getBlackUser(String startDate, String endDate, String userName, Integer lockReasonType);

	List<Map<String, Object>> getBlackUserDayGroup(String startDate, String endDate, String userName,
			Integer lockReasonType);

	List<Map<String, Object>> getBlackUserHourGroup(String startDate, String endDate, String userName,
			Integer lockReasonType);

	List<Map<String, Object>> getBlackUserReason();

	List<Map<String, Object>> getUnBlackUser(String startDate, String endDate, String userName);

	List<Map<String, Object>> getUnBlackUserDayGroup(String startDate, String endDate,String userName);

	List<Map<String, Object>> getUnBlackUserHourGroup(String startDate, String endDate, String userName);

	List<Map<String, Object>> getUnBlackUserByTime(String time);

	List<Map<String, Object>> getUnBlackUserByDay(String time);

	List<Map<String, Object>> getYiChangUser(String startDate, String endDate, String userName, Integer lockReasonType);

	 int updateYiChangUser(String orderId,Integer type);

}
