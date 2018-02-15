package com.qianwang.service.assistant.impl;

import com.qianwang.dao.domain.assistant.UserLastSubscriptionDataAssistant;
import com.qianwang.dao.domain.assistant.UserSubscriptionDataAssistant;
import com.qianwang.mapper.assistant.UserSubscriptionMapper;
import com.qianwang.service.assistant.UserSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huguo on 2016/10/13.
 */
@Service
public class UserSubScriptionServiceImpl implements UserSubscriptionService {

    protected static final Logger LOG = LoggerFactory.getLogger(UserSubScriptionServiceImpl.class);

    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;

   public List<UserSubscriptionDataAssistant> getUserSubscripionInfos(Integer userId, String userName, Integer payType, String startDate, String endDate,Integer start,Integer length)
   {
       Map<String,Object> params = new HashMap();
       params.put("userId",userId);
       params.put("userName",userName);
       params.put("payType",payType);
       params.put("startDate",startDate);
       params.put("endDate",endDate);
       params.put("start",start);
       params.put("length",length);
      return userSubscriptionMapper.getUserSubscripionInfos(params);
   }

/*    public int selectlastCount(Integer userId, String userName, Integer payType, String startDate,String endDate)
    {
        Map<String,Object> params = new HashMap();
        params.put("userId",userId);
        params.put("userName",userName);
        params.put("payType",payType);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        return userSubscriptionMapper.selectlastCount(params);
    }*/

    public int getUserSubscriptionCount(Integer userId, String userName, Integer payType, String startDate,String endDate)
    {
        Map<String,Object> params = new HashMap();
        params.put("userId",userId);
        params.put("userName",userName);
        params.put("payType",payType);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        return userSubscriptionMapper.getUserSubscriptionCount(params);
    }


    public List<UserLastSubscriptionDataAssistant> getUserLastSubscripionInfos(Integer userId, String userName, Integer payType, String startDate, String endDate, Integer start, Integer length)
    {
        Map<String,Object> params=new HashMap();
        params.put("userId",userId);
        params.put("userName",userName);
        params.put("payType",payType);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("start",start);
        params.put("length",length);
        return userSubscriptionMapper.getUserLastSubscripionInfos(params);
    }
}
