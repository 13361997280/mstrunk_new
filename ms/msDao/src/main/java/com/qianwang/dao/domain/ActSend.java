package com.qianwang.dao.domain;

import java.util.Date;

public class ActSend {
    private Integer id;

    private String name;

    private Byte sendTunnel;

    private Integer tunnelId;

    private String tunnelName;

    private Integer selectNum;

    private Integer actNum;

    private Byte status;

    private Date createTime;

    private Date sendTime;

    private Integer messageId;

    private String userSelectGroupId;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getSendTunnel() {
        return sendTunnel;
    }

    public void setSendTunnel(Byte sendTunnel) {
        this.sendTunnel = sendTunnel;
    }

    public Integer getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(Integer tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public Integer getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(Integer selectNum) {
        this.selectNum = selectNum;
    }

    public Integer getActNum() {
        return actNum;
    }

    public void setActNum(Integer actNum) {
        this.actNum = actNum;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getUserSelectGroupId() {
        return userSelectGroupId;
    }

    public void setUserSelectGroupId(String userSelectGroupId) {
        this.userSelectGroupId = userSelectGroupId;
    }
}