package com.qianwang.dao.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单用户
 * 
 * @author tangtaiquan
 *
 */
public class UnusualUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 异常类型
	 */
	private Integer dealResult;

	/**
	 * 用户ID
	 */
	private String userId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getDealResult() {
		return dealResult;
	}

	public void setDealResult(Integer dealResult) {
		this.dealResult = dealResult;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
