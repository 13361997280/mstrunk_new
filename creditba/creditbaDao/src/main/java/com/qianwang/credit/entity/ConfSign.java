package com.qianwang.credit.entity;

/**
 * 签到配置
 * @author wangjg
 *
 */
public class ConfSign extends BaseBizEntity{

	private static final long serialVersionUID = 1L;
	
	public static int STATUS_NORMAL = 0; //正常
	public static int STATUS_OFF = 1; //下架

	private Integer id; //主键

	private Double score; //签到加分

	private Double totalScoreLimit; //总分上限 无上限传空

	private String memo; //备注

	private Integer status; //状态 0为正常  1为已下架

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Double getTotalScoreLimit() {
		return totalScoreLimit;
	}
	public void setTotalScoreLimit(Double totalScoreLimit) {
		this.totalScoreLimit = totalScoreLimit;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}