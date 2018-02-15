package com.qianwang.dao.domain.user;

import java.util.Date;

public class UserAnalyseStat {

	private Integer userId;
	private String username;
	private Integer isClient;
	private Integer userType;
	private Integer presenterId;
	private Integer presentUserNum;
	private Date registDate;
	private Integer cookieUserNum;
	private Integer ipUserNum;
	private Integer fingerPrintUserNum;
	private Integer deviceUserNum;
	
	private Integer departmentId;
	private String departmentName;
	private Integer spreadTypeId;
	private String spreadTypeName;
	private Integer districtId;
	private String districtName;
	private String bank;
	private String subBank;
	private String cardNum;
	private Integer sequentCardCount;
	private Integer cardCount;
	private Integer syncGroup;
	
	public Integer getIsClient() {
		return isClient;
	}
	public void setIsClient(Integer isClient) {
		this.isClient = isClient;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getSubBank() {
		return subBank;
	}
	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public Integer getSequentCardCount() {
		return sequentCardCount;
	}
	public void setSequentCardCount(Integer sequentCardCount) {
		this.sequentCardCount = sequentCardCount;
	}
	public Integer getCardCount() {
		return cardCount;
	}
	public void setCardCount(Integer cardCount) {
		this.cardCount = cardCount;
	}
	public Integer getSyncGroup() {
		return syncGroup;
	}
	public void setSyncGroup(Integer syncGroup) {
		this.syncGroup = syncGroup;
	}
	public Integer getPresentUserNum() {
		return presentUserNum;
	}
	public void setPresentUserNum(Integer presentUserNum) {
		this.presentUserNum = presentUserNum;
	}
	public Integer getPresenterId() {
		return presenterId;
	}
	public void setPresenterId(Integer presenterId) {
		this.presenterId = presenterId;
	}
	public Integer getDeviceUserNum() {
		return deviceUserNum;
	}
	public void setDeviceUserNum(Integer deviceUserNum) {
		this.deviceUserNum = deviceUserNum;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Integer getSpreadTypeId() {
		return spreadTypeId;
	}
	public void setSpreadTypeId(Integer spreadTypeId) {
		this.spreadTypeId = spreadTypeId;
	}
	public String getSpreadTypeName() {
		return spreadTypeName;
	}
	public void setSpreadTypeName(String spreadTypeName) {
		this.spreadTypeName = spreadTypeName;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public Integer getCookieUserNum() {
		return cookieUserNum;
	}
	public void setCookieUserNum(Integer cookieUserNum) {
		this.cookieUserNum = cookieUserNum;
	}
	public Integer getIpUserNum() {
		return ipUserNum;
	}
	public void setIpUserNum(Integer ipUserNum) {
		this.ipUserNum = ipUserNum;
	}
	public Integer getFingerPrintUserNum() {
		return fingerPrintUserNum;
	}
	public void setFingerPrintUserNum(Integer fingerPrintUserNum) {
		this.fingerPrintUserNum = fingerPrintUserNum;
	}
}
