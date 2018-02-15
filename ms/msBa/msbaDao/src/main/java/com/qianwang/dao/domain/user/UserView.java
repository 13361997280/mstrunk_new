package com.qianwang.dao.domain.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 用户信息
 * @author zou
 *
 */
public class UserView implements Serializable{

	public boolean isBindMobile() {
		return isBindMobile;
	}

	public void setIsBindMobile(boolean isBindMobile) {
		this.isBindMobile = isBindMobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 部门编码
	 * 
	 * @author zou
	 * 
	 */
	static enum Department{
		EMPTY("请选择"), BOSS("总裁"), DATA_CENTER("数据部"), DEVELOPER("研发部"), PRODUCT("产品部"), CUSTOMER_SERVICE("客服部"), MAINTAIN(
				"运维部"), MARKET("市场部"), STRATEGY("战略部"), TEST("测试部"), FINANCE("财务部");

		private String name;

		private Department(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static String getName(int o) {
			Department[] departs = Department.values();
			for (Department department : departs) {
				if (department.ordinal() == o) {
					return department.getName();
				}
			}
			return EMPTY.getName();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -575880400467200652L;
	
	/**
	 * 用户名称
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 真实名称
	 */
	private String realname;
	
	/**
	 * 所属部门id
	 */
	private int departmentId;
	
	/**
	 * 用户所属部门
	 */
	@SuppressWarnings("unused")
	private String department;
	
	/**
	 * 用户创建时间
	 */
	private Date createTime;
	
	/**
	 * 启用或停用改用户
	 */
	private boolean enabled;

	private boolean modifyPwd;

	public boolean isModifyPwd() {
		return modifyPwd;
	}

	public void setModifyPwd(boolean modifyPwd) {
		this.modifyPwd = modifyPwd;
	}

	private boolean isBindMobile;
	private String mobile;
	/**
	 * 用户权限集合
	 */
	private List<UserAuthority> authorities;
	
	/**
	 * 创建人
	 */
	private String createUsername;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	} 
	
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return Department.getName(departmentId);
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
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the departmentId
	 */
	public int getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the authorities
	 */
	public List<UserAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(List<UserAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUsername() {
		return createUsername;
	}

	/**
	 * @param createUsername the createUserName to set
	 */
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
