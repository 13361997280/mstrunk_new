package com.qianwang.web.controller.assistant;

import com.qianwang.dao.domain.assistant.UserLastSubscriptionDataAssistant;
import com.qianwang.dao.domain.assistant.UserSubscriptionDataAssistant;
import com.qianwang.service.assistant.UserSubscriptionService;
import com.qianwang.util.lang.DateUtil;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by huguo on 2016/10/13.
 * 订阅用户画像信息查询
 */
@Controller
@RequestMapping("/usersubscription")
public class UserSubscriptionController extends BaseController {
    protected static final Log LOG = LogFactory.getLog(UserSubscriptionController.class);

    @Autowired
    private UserSubscriptionService userSubscriptionService;


    @RequestMapping( value = "/getUserSubscription",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getUserSubscription(@RequestParam(required = false, defaultValue = "0") Integer start,
                                          @RequestParam(required = false, defaultValue = "10") Integer length,
                                          @RequestParam(required = false) Integer userId,
                                          @RequestParam(required = false) String userName,
                                          @RequestParam(required = false) Integer payType,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate)
    {

        try
        {
            String currentUserName = getCurrentUsername();
            Map<String,String> maps = DateUtil.getDefaultDate();

            if(startDate == null || startDate == "")
            {
                startDate = maps.get("startDate");
            }

            if(endDate == null || endDate == "")
            {
                endDate = maps.get("endDate");
            }
            LOG.info(currentUserName+"正在进行订阅数据查询操作,查询的时间范围:开始时间:"+startDate+"--结束时间:"+endDate);
			
            int count = userSubscriptionService.getUserSubscriptionCount(userId,userName,payType,startDate,endDate);
           // length = 0;
            List<UserSubscriptionDataAssistant> subscriptions = userSubscriptionService.getUserSubscripionInfos(userId,userName,payType,startDate,endDate,start,length);
			//int count =subscriptions.size();
            LOG.info(currentUserName+"订阅用户画像查询操作结束");
            return AjaxResult.successResult(count,count,subscriptions);
        }
        catch (Exception ex)
        {
            LOG.error(ex.getStackTrace());
            return AjaxResult.failureResult(null);
        }


    }
    @RequestMapping(value = "/getUserLastSubscription",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getUserLastSubscripionInfos(@RequestParam(required = false,defaultValue = "0") Integer start,
                                                  @RequestParam(required = false,defaultValue = "10") Integer length,
                                                  @RequestParam(required = false) Integer userId,
                                                  @RequestParam(required = false) String userName,
                                                  @RequestParam(required = false) Integer payType,
                                                  @RequestParam(required = false) String startDate,
                                                  @RequestParam(required = false) String endDate)
    {

        try{
            String currentUserName = getCurrentUsername();

            Map<String,String> maps = DateUtil.getDefaultDate();

            if(startDate == null || startDate == "")
            {
                startDate = maps.get("startDate");
            }

            if(endDate == null || endDate == "")
            {
                endDate = maps.get("endDate");
            }
            LOG.info(currentUserName+"正在进行退订数据查询操作,查询的时间范围:开始时间:"+startDate+"--结束时间:"+endDate);

            length =0;
            List<UserLastSubscriptionDataAssistant> lastSubscriptions = userSubscriptionService.getUserLastSubscripionInfos(userId,userName,payType,startDate,endDate,start,length);
            int count= lastSubscriptions.size();

            LOG.info(currentUserName+"退订数据查询操作结束");

            return AjaxResult.successResult(count,count,lastSubscriptions);
        }
        catch (Exception ex)
        {
            LOG.error(ex.getStackTrace());
            return AjaxResult.failureResult(null);
        }
    }
}
