package com.qianwang.dao.domain.authority;

import java.io.Serializable;
/**
 * 菜单权限对应关系
 * @author zou
 *
 */
public class MenuAuthority implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4132950355446081677L;
	
	/**
	 * 菜单ID
	 */
	private int menuId;
	
	/**
	 * 权限code
	 */
	private String authorityCode;
	
	/**
	 * 菜单名称
	 */
	private String menuName;
	
	/**
	 * 权限名称
	 */
	private String authorityName;

	/**
	 * @return the menuId
	 */
	public int getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

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
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
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
}
