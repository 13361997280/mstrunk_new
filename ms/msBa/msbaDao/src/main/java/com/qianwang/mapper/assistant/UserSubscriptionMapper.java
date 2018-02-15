package com.qianwang.mapper.assistant;

import com.qianwang.dao.domain.assistant.UserLastSubscriptionDataAssistant;
import com.qianwang.dao.domain.assistant.UserSubscriptionDataAssistant;

import java.util.List;
import java.util.Map;

/**
 * Created by huguo on 2016/10/13.
 */
public interface UserSubscriptionMapper {
    List<UserSubscriptionDataAssistant> getUserSubscripionInfos(Map<String, Object> params);

    int getUserSubscriptionCount(Map<String, Object> params);

  /*  int selectlastCount(Map<String, Object> params);*/

    List<UserLastSubscriptionDataAssistant> getUserLastSubscripionInfos(Map<String, Object> params);
}
