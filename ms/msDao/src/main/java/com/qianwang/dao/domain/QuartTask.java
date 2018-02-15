package com.qianwang.dao.domain;

import com.qianwang.util.lang.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class QuartTask implements Serializable{
    private Integer id;

    private String taskName;

    private Integer userId;

    private Integer messageId;

    private Integer activityId;

    private String userSelectId;

    private Date cornTime;

    private String cronExpression;

    private boolean enable;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getUserSelectId() {
        return userSelectId;
    }

    public void setUserSelectId(String userSelectId) {
        this.userSelectId = userSelectId;
    }

    public Date getCornTime() {
        return cornTime;
    }

    public void setCornTime(Date cornTime) {
        this.cornTime = cornTime;
        this.cronExpression = DateUtil.formatDate(cornTime, "ss mm HH dd MM ? yyyy");
    }

    public void translate(Date date) {
        this.cronExpression = DateUtil.formatDate(date, "ss mm HH dd MM ? yyyy");
    }

}