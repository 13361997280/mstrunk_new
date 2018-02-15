package com.qianwang.credit.entity;

import java.util.Date;

/**
 * 任务配置
 * @author wangjg
 *
 */
public class ConfTaskSet extends BaseBizEntity {
	
	public static int STATUS_NORMAL = 0; //正常

	private Integer id; //自增ID

	private Double onedayScore;

	private Double totalScore;

	private Date createTime; //创建时间

	private Date updateTime; //修改时间
	private String operator;
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getOnedayScore() {
		return onedayScore;
	}

	public void setOnedayScore(Double onedayScore) {
		this.onedayScore = onedayScore;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}