package com.qianwang.web.controller.marketing;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qianwang.dao.domain.ActSend;
import com.qianwang.dao.domain.Message;
import com.qianwang.dao.domain.QuartTask;
import com.qianwang.mapper.QuartTaskMapper;
import com.qianwang.mapper.UserSelectGroupMapper;
import com.qianwang.service.marketing.ActivityService;
import com.qianwang.service.marketing.MessageService;
import com.qianwang.service.quartz.LoadTask;
import com.qianwang.service.redis.RedisUtil;
import com.qianwang.util.lang.DateUtil;
import com.qianwang.web.auth.CheckEnum;
import com.qianwang.web.auth.MsAuth;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import com.qianwang.web.util.UrlUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 活动模块
 *
 * @author song.j
 * @create 2017 -05-16 10:10:05
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController{


    private final static Logger LOG = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    ActivityService activityService;

    @Autowired
    MessageService messageService;

    @Autowired
    QuartTaskMapper quartTaskMapper;

    @Autowired
    LoadTask loadTask;

    @Autowired
    UserSelectGroupMapper userSelectGroupMapper;

    @Autowired
    RedisUtil redisUtil;
    /**
     * 分页查询活动
     *
     * @param page the page
     * @param size the size
     * @return the object
     */
    @RequestMapping("/list")
    @ResponseBody
    public AjaxResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer
            size) {
        //最多一次只能查50条
        if (size > 50)
            size = 50;

        Integer userId = getCurrentUserId();
        Map pageData = activityService.getActivityByPage(page, size,userId);

        return AjaxResult.successResult(pageData);
    }

    /**
     * 短信页面的提交。
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @MsAuth(role = CheckEnum.CHECK)
    public AjaxResult save(HttpServletRequest request,@RequestBody JSONObject jsonObject) {

        ActSend actSend = JSONObject.toJavaObject(jsonObject,ActSend.class);
        Message message = JSONObject.toJavaObject(jsonObject,Message.class);

        if (message.getSendTime().getHours() < 8) {
            return AjaxResult.failureResult("此时间段不能推送短信！");
        }

        if (jsonObject.get("token") != null && redisUtil.keyExistSet(jsonObject.get("token").toString())) {
            return AjaxResult.failureResult("请不要重复提交");
        }

        int sendType = Integer.valueOf(jsonObject.get("sendType").toString()) ;

        int userId = getCurrentUserId();
        actSend.setUserId(userId);
        actSend.setSendTunnel(message.getType());
        actSend.setCreateTime(new Date());
        activityService.saveActivity(actSend);

        message.setUserId(userId);
        message.setCreateTime(new Date());
        message.setUpdateTime(message.getCreateTime());
        message.setActivityId(actSend.getId());

        Map resultData = new HashMap<String, Integer>();
        resultData.put("activityId", actSend.getId());

        //获取消息内容包含的url链接
        String contentUrl = null;

        if (message.getMessageId() == null){
            //如果消息ID为null。则根据内容中包含的url正规匹配搜索消息id
            if (message.getShortContent() != null){
                try {
                    contentUrl = UrlUtil.getUrlOne(message.getShortContent());
                } catch (IllegalAccessException e) {
                    LOG.info("shortContent has more url, shortContent = {}", message.getShortContent());
                }
            }
            Integer mesId = null;
            if (contentUrl != null){
                mesId = messageService.hasShortContent(contentUrl);
            }
            message.setMessageId(mesId);
        }

        messageService.saveMessage(message);

        if (message.getSendTime() != null) {

            QuartTask quartTask = new QuartTask();
            if (sendType == 1) {
                quartTask.setCornTime(DateUtil.dateCompute(message.getSendTime(), "MINUTE", 10));
            } else {
                quartTask.setCornTime(message.getSendTime());
            }

            quartTask.setTaskName(actSend.getName());
            quartTask.setUserId(userId);
            quartTask.setMessageId(message.getId());
            quartTask.setActivityId(actSend.getId());
            quartTask.setUserSelectId(actSend.getUserSelectGroupId());
            quartTask.translate(quartTask.getCornTime());
            quartTask.setEnable(true);
            quartTaskMapper.insertSelective(quartTask);
            try {
                loadTask.addTask(quartTask);
            } catch (SchedulerException e) {
                LOG.error(e.getMessage());
            }

        }
        return AjaxResult.success();
    }

    /**
     * h5页面的提交
     */
    @RequestMapping(value = "/h5/save", method = RequestMethod.POST)
    @ResponseBody
    @MsAuth(role = CheckEnum.CHECK)
    public AjaxResult h5Save(@RequestBody JSONObject jsonObject) {

        ActSend actSend = JSONObject.toJavaObject(jsonObject,ActSend.class);
        Message message = JSONObject.toJavaObject(jsonObject,Message.class);

        if (StringUtils.isNotEmpty(message.getLink())){
            if (message.getLink().length() > 200){
                return AjaxResult.failureResult("输入的url长度请不要超过200");
            }

//            if (!UrlUtil.invokeUrl(message.getLink())){
//                return AjaxResult.failureResult("请填写正常的url 如：http://www.baidu.com");
//            }
        }

        if (jsonObject.get("token") != null && redisUtil.keyExistSet(jsonObject.get("token").toString())) {
            return AjaxResult.failureResult("请不要重复提交");
        }

        int userId = getCurrentUserId();
        actSend.setUserId(userId);
        actSend.setSendTunnel(message.getType());
        actSend.setCreateTime(new Date());
        actSend.setStatus((byte) 1);
        activityService.saveActivity(actSend);

        message.setUserId(userId);
        message.setCreateTime(new Date());
        message.setUpdateTime(message.getCreateTime());
        message.setActivityId(actSend.getId());
        message.setShortContent(JSON.toJSONString(jsonObject.get("shortContent")));
        messageService.saveMessage(message);

        Map resultMap = new HashMap();
        resultMap.put("messageId", message.getId());
        return AjaxResult.successResult(resultMap);
    }
}
