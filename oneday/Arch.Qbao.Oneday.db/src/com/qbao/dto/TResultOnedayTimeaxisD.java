package com.qbao.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TResultOnedayTimeaxisD {
    private Long id;

    private Date statDate;

    private Integer hour;

    private Integer userId;

    private BigDecimal score;

    private String etlTime;


    public TResultOnedayTimeaxisD() {
    }

    public TResultOnedayTimeaxisD( Date statDate, Integer hour, Integer userId, BigDecimal score, String etlTime) {
        this.statDate = statDate;
        this.hour = hour;
        this.userId = userId;
        this.score = score;
        this.etlTime = etlTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getEtlTime() {
        return etlTime;
    }

    public void setEtlTime(String etlTime) {
        this.etlTime = etlTime;
    }
}