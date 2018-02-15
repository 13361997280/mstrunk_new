package com.qianwang.service.assistant;

import com.qianwang.dao.domain.assistant.UserLastSubscriptionDataAssistant;
import com.qianwang.dao.domain.assistant.UserSubscriptionDataAssistant;

import java.util.List;

/**
 * Created by huguo on 2016/10/13.
 */
public interface UserSubscriptionService {
    List<UserSubscriptionDataAssistant> getUserSubscripionInfos(Integer userId, String userName, Integer payType, String startDate,String endDate,Integer start,Integer length);

    int getUserSubscriptionCount(Integer userId, String userName, Integer payType, String startDate,String endDate);

/*
    int selectlastCount(Integer userId, String userName, Integer payType, String startDate,String endDate);
*/

    List<UserLastSubscriptionDataAssistant> getUserLastSubscripionInfos(Integer userId, String userName, Integer payType, String startDate, String endDate, Integer start, Integer length);
}
