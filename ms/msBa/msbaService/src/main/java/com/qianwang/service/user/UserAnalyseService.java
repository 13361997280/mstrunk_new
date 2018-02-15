package com.qianwang.service.user;

import java.util.Date;
import java.util.List;

import com.qianwang.dao.domain.user.UserAnalyseStat;

public interface UserAnalyseService {

	List<UserAnalyseStat> selectUserAnalyseStat(Date startDate, Date endDate, Integer presentUserId,
			Integer downCookieNum, Integer upCookieNum, 
			Integer downIpNum, Integer upIpNum,
			Integer downFpNum, Integer upFpNum,
			Integer downDevIdNum, Integer upDevIdNum,
			Integer downSequnetCardNum, Integer upSequnetCardNum,
			Integer downCardNum, Integer upCardNum,
			Integer downPresentNum, Integer upPresentNum,
			
			Integer channelId,
			Integer spreadTypeId,
			Integer districtId,
			Integer start, Integer length, String sort);
	
	Integer countUserAnalyseStat(Date startDate, Date endDate, Integer presentUserId,
			Integer downCookieNum, Integer upCookieNum, 
			Integer downIpNum, Integer upIpNum,
			Integer downFpNum, Integer upFpNum,
			Integer downDevIdNum, Integer upDevIdNum,
			Integer downSequnetCardNum, Integer upSequnetCardNum,
			Integer downCardNum, Integer upCardNum,
			Integer downPresentNum, Integer upPresentNum,
			
			Integer channelId,
			Integer spreadTypeId,
			Integer districtId);
}
