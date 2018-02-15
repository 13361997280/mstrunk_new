package com.qbao.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BusScore {
    private Integer id;

    private Integer userId;

    private BigDecimal ratio;

    private BigDecimal totalScore;

    private BigDecimal creditScore;

    private BigDecimal addScore;

    private Integer status;

    private Date addTime;

    private BigDecimal adjustScore;

    private Date adjustTime;

    private BigDecimal signScore;

    private BigDecimal taskScore;

    private Integer signFre;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(BigDecimal creditScore) {
        this.creditScore = creditScore;
    }

    public BigDecimal getAddScore() {
        return addScore;
    }

    public void setAddScore(BigDecimal addScore) {
        this.addScore = addScore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public BigDecimal getAdjustScore() {
        return adjustScore;
    }

    public void setAdjustScore(BigDecimal adjustScore) {
        this.adjustScore = adjustScore;
    }

    public Date getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }

    public BigDecimal getSignScore() {
        return signScore;
    }

    public void setSignScore(BigDecimal signScore) {
        this.signScore = signScore;
    }

    public BigDecimal getTaskScore() {
        return taskScore;
    }

    public void setTaskScore(BigDecimal taskScore) {
        this.taskScore = taskScore;
    }

    public Integer getSignFre() {
        return signFre;
    }

    public void setSignFre(Integer signFre) {
        this.signFre = signFre;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}