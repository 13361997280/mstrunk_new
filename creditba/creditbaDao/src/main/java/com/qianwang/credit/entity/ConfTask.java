package com.qianwang.credit.entity;

/**
 * 任务配置
 * @author wangjg
 *
 */
public class ConfTask extends BaseBizEntity{
	
	private static final long serialVersionUID = 1L;

	public static int STATUS_NORMAL = 0; //正常

	private Integer id; //自增ID

	private Integer taskId; //任务id

	private Double addScore; //任务加分

	private Double subScore; //提前结束任务惩罚:0 无惩罚，>0 惩罚数额

	private String isDuplicate; //是否可重复 n为不重复，y为重复

	private String memo; //备注

	private Integer status; //状态 :0-正常, 1-下架

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Double getAddScore() {
		return addScore;
	}

	public void setAddScore(Double addScore) {
		this.addScore = addScore;
	}

	public Double getSubScore() {
		return subScore;
	}

	public void setSubScore(Double subScore) {
		this.subScore = subScore;
	}

	public String getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}