package com.qianwang.dao.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单用户
 * 
 * @author tangtaiquan
 *
 */
public class BlackUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 封号原因
	 */
	private String reasonDetail;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 操作时间
	 */
	private Date operateTime;

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the reasonDetail
	 */
	public String getReasonDetail() {
		return reasonDetail;
	}

	/**
	 * @param reasonDetail
	 *            the reasonDetail to set
	 */
	public void setReasonDetail(String reasonDetail) {
		this.reasonDetail = reasonDetail;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the operateTime
	 */
	public Date getOperateTime() {
		return operateTime;
	}

	/**
	 * @param operateTime
	 *            the operateTime to set
	 */
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

}
