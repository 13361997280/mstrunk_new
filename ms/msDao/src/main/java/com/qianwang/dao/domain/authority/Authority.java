package com.qianwang.dao.domain.authority;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限信息
 * @author zou
 *
 */
public class Authority implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5857149978799112791L;

	/**
	 * 权限编码
	 */
	private String authorityCode;
	
	/**
	 * 权限名称
	 */
	private String authorityName;
	
	/**
	 * 权限创建时间
	 */
	private Date createTime;
	
	/**
	 * 权限创建人
	 */
	private String createUser;

	/**
	 * @return the authorityCode
	 */
	public String getAuthorityCode() {
		return authorityCode;
	}

	/**
	 * @param authorityCode the authorityCode to set
	 */
	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	/**
	 * @return the authorityName
	 */
	public String getAuthorityName() {
		return authorityName;
	}

	/**
	 * @param authorityName the authorityName to set
	 */
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}
