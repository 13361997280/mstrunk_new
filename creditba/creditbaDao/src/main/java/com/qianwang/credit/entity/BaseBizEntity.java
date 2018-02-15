package com.qianwang.credit.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author wangjg
 *
 */
public class BaseBizEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected Date createTime; //创建时间

	protected Date updateTime; //修改时间
	
	protected String operator; //操作者

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

}
