package com.qbao.dto;

import java.math.BigDecimal;

public class ConfTaskSet {
    private BigDecimal onedayScore;
    private BigDecimal totalScore;

    public BigDecimal getOnedayScore() {
        return onedayScore;
    }

    public void setOnedayScore(BigDecimal onedayScore) {
        this.onedayScore = onedayScore;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
}