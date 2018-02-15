package com.qianwang.dao.domain.confbaseset;

import java.math.BigDecimal;
import java.util.Date;

public class ConfBaseSet {
    private Integer id;

    private BigDecimal onedayScoreLimit;

    private Integer status;

    private String operator;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getOnedayScoreLimit() {
        return onedayScoreLimit;
    }

    public void setOnedayScoreLimit(BigDecimal onedayScoreLimit) {
        this.onedayScoreLimit = onedayScoreLimit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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