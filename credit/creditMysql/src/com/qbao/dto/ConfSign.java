package com.qbao.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ConfSign {

    private BigDecimal score;

    private BigDecimal totalScoreLimit;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getTotalScoreLimit() {
        return totalScoreLimit;
    }

    public void setTotalScoreLimit(BigDecimal totalScoreLimit) {
        this.totalScoreLimit = totalScoreLimit;
    }
}