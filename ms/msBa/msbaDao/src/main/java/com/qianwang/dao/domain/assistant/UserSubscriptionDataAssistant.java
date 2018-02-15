package com.qianwang.dao.domain.assistant;


import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huguo on 2016/10/13.
 * 用户订阅用户画像信息
 */
public class UserSubscriptionDataAssistant {
    private Integer num;
    private Integer userId;
    private String userName;
    //第一次订阅日期
    private Date firstSubscriptionDate;
    private Integer payType;
    //订阅日期
    private Date subscriptionDate;


    //流水订阅金额
    private BigInteger payCost;

    private String payTypeDesc;

    private Integer penalties;
    public UserSubscriptionDataAssistant()
    {

    }
    public UserSubscriptionDataAssistant(Integer penalties,String payTypeDesc,BigInteger payCost,Integer num,Integer userId,String userName,Date firstSubscriptionDate,Integer payType,Date subscriptionDate)
    {
        this.penalties =penalties;
        this.payTypeDesc = payTypeDesc;

        this.payCost=payCost;
        this.num=num;
        this.userId=userId;
        this.userName=userName;
        this.firstSubscriptionDate=firstSubscriptionDate;
        this.payType=payType;
        this.subscriptionDate = subscriptionDate;
    }

    public void setPenalties(Integer penalties){this.penalties = penalties;}

    public Integer getPenalties(){return this.penalties;}
    public  void setPayTypeDesc(String payTypeDesc)
    {
        this.payTypeDesc=payTypeDesc;
    }
    public String getPayTypeDesc()
    {
        return this.payTypeDesc;
    }

    public void setPayCost(BigInteger payCost)
    {
        this.payCost=payCost;
    }
    public BigInteger getPayCost()
    {
        return this.payCost;
    }
    public void setNum(Integer num)
    {
        this.num=num;
    }
    public Integer getNum()
    {
        return this.num;
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

    public void setPayType(Integer payType)
    {
        this.payType= payType;
    }

    public Integer getPayType()
    {
        return this.payType;
    }

    public void setFirstSubscriptionDate(Date firstSubscriptionDate)
    {
        this.firstSubscriptionDate=firstSubscriptionDate;
    }

    public Date getFirstSubscriptionDate()
    {
        return this.firstSubscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate)
    {
        this.subscriptionDate=subscriptionDate;
    }

    public Date getSubscriptionDate()
    {
        return this.subscriptionDate;
    }
}
