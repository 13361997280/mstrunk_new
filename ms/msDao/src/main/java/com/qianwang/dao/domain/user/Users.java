package com.qianwang.dao.domain.user;

import java.io.Serializable;
import java.util.Date;


public class Users implements Serializable{
	private Integer id;

	private String username;

	private String password;

	private boolean enabled;

	private String mobile;

	private String groupIds;

	private String email;

	private Date createTime;

	private Date updateTime;

	public Users() {
	}

	public Users(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Users(String username, String password, Boolean enabled, String mobile, String groupIds, String email, Date createTime, Date updateTime) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.mobile = mobile;
		this.groupIds = groupIds;
		this.email = email;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Users(Integer id, String username, String password, Boolean enabled, String mobile, String groupIds, String email, Date createTime, Date updateTime) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.mobile = mobile;
		this.groupIds = groupIds;
		this.email = email;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds == null ? null : groupIds.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
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

	@Override
	public String toString() {
		return "Users{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", enabled=" + enabled +
				", mobile='" + mobile + '\'' +
				", groupIds='" + groupIds + '\'' +
				", email='" + email + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}