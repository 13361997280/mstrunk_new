package com.qianwang.credit.entity;

import java.util.Date;

/**
 * 业务信用分配置表
 * @author wangjg
 *
 */
public class ConfBus {
	public static int STATUS_NORMAL = 0; //正常
	public static int STATUS_OFF = 1; //下架
	
	/* 
	 * 业务id(自增) 
	 * @Id
	 */
	private Integer busId; 

	private String busName; //业务名称

	private String memo; //备注

	private Integer status; //状态 0正常 1已下架

	private String link; //跳转链接

	private Date createTime; //创建时间

	private Date updateTime; //修改时间

	public Integer getBusId() {
		return busId;
	}
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}