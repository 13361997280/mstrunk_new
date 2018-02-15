package com.qianwang.dao.domain.user;

import java.io.Serializable;

/**
 * 用户权限
 * 
 * @author zou
 * 
 */
public class UserAuthority implements Serializable {
	
	public final static String ROLE_USER = "ROLE_USER";
	
	public final static String ROLE_ADMIN = "ROLE_ADMIN";
	
	public final static String ROLE_GAIN_ADMIN = "ROLE_GAIN_ADMIN";
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2614827814003383496L;

	/**
	 * 权限code
	 */
	private String authorityCode;

	/**
	 * 权限名称
	 */
	private String authorityName;

	/**
	 * 用户名称
	 */
	private String username;

	/**
	 * @return the authorityCode
	 */
	public String getAuthorityCode() {
		return authorityCode;
	}

	/**
	 * @param authorityCode
	 *            the authorityCode to set
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
