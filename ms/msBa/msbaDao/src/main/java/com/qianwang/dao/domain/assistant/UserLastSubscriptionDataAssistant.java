package com.qianwang.dao.domain.assistant;

import java.util.Date;

/**
 * Created by huguo on 2016/10/15.
 */
public class UserLastSubscriptionDataAssistant {
    private Integer userId;
    private String userName;
    private String payTypeDesc;
    //未订阅日期
    private Date lastSubscriptionDate;

    public UserLastSubscriptionDataAssistant()
    {

    }
    public UserLastSubscriptionDataAssistant(Integer userId,String userName,String payTypeDesc,Date lastSubscriptionDate)
    {
        this.userId=userId;
        this.userName=userName;
        this.payTypeDesc=payTypeDesc;
        this.lastSubscriptionDate = lastSubscriptionDate;
    }
    public void setUserId(Integer userId)
    {
        this.userId=userId;
    }
    public Integer getUserId()
    {
        return this.userId;
    }

    public void setUserName(String userName)
    {
        this.userName =userName;
    }
    public String getUserName()
    {
        return  this.userName;
    }

    public void setPayTypeDesc(String payTypeDesc)
    {
        this.payTypeDesc= payTypeDesc;
    }

    public String getPayTypeDesc()
    {
        return this.payTypeDesc;
    }

    public void setLastSubscriptionDate(Date unSubscriptionDate)
    {
        this.lastSubscriptionDate=unSubscriptionDate;
    }

    public Date getLastSubscriptionDate()
    {
        return this.lastSubscriptionDate;
    }
}
