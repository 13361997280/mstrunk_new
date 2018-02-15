package com.qianwang.dao.domain.user;

import java.util.Date;

public class UserRegist {

	private Date statDate;
	
	private int statHour;
	
	private int userNum;
	
	private int recommendedNum;
	
	private int noRecommendedNum;

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public int getStatHour() {
		return statHour;
	}

	public void setStatHour(int statHour) {
		this.statHour = statHour;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getRecommendedNum() {
		return recommendedNum;
	}

	public void setRecommendedNum(int recommendedNum) {
		this.recommendedNum = recommendedNum;
	}

	public int getNoRecommendedNum() {
		return noRecommendedNum;
	}

	public void setNoRecommendedNum(int noRecommendedNum) {
		this.noRecommendedNum = noRecommendedNum;
	}
	
	
}
