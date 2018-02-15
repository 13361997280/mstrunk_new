package com.qianwang.service.user.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianwang.dao.domain.user.UserAnalyseStat;
import com.qianwang.mapper.user.UserRegistMapper;
import com.qianwang.service.user.UserAnalyseService;
@Service
public class UserAnalyseServiceImpl implements UserAnalyseService {

	@Autowired
	private UserRegistMapper userRegistMapper;

	@Override
	public List<UserAnalyseStat> selectUserAnalyseStat(Date startDate, Date endDate, Integer presentUserId, 
			Integer downCookieNum, Integer upCookieNum,
			Integer downIpNum, Integer upIpNum, Integer downFpNum,
			Integer upFpNum, Integer downDevIdNum, Integer upDevIdNum,
			Integer downSequnetCardNum, Integer upSequnetCardNum,
			Integer downCardNum, Integer upCardNum, Integer downPresentNum,
			Integer upPresentNum, Integer channelId, Integer spreadTypeId,
			Integer districtId, Integer start, Integer length, String sort) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("downCookieNum", downCookieNum);
		params.put("upCookieNum", upCookieNum);
		params.put("downIpNum", downIpNum);
		params.put("upIpNum", upIpNum);
		params.put("downFpNum", downFpNum);
		params.put("upFpNum", upFpNum);
		params.put("downDevIdNum", downDevIdNum);
		params.put("upDevIdNum", upDevIdNum);
		params.put("downSequnetCardNum", downSequnetCardNum);
		params.put("upSequnetCardNum", upSequnetCardNum);
		params.put("downCardNum", downCardNum);
		params.put("upCardNum", upCardNum);
		params.put("downPresentNum", downPresentNum);
		params.put("upPresentNum", upPresentNum);
		
		params.put("channelId", channelId);
		params.put("spreadTypeId", spreadTypeId);
		params.put("districtId", districtId);
		
		params.put("start", start);
		params.put("length", length);
		params.put("sort", sort);
		
		params.put("presentUserId", presentUserId);
		return userRegistMapper.selectRegistUserAnalyseStat(params);
	}

	@Override
	public Integer countUserAnalyseStat(Date startDate, Date endDate, Integer presentUserId,
			Integer downCookieNum, Integer upCookieNum, Integer downIpNum,
			Integer upIpNum, Integer downFpNum, Integer upFpNum,
			Integer downDevIdNum, Integer upDevIdNum,
			Integer downSequnetCardNum, Integer upSequnetCardNum,
			Integer downCardNum, Integer upCardNum, Integer downPresentNum,
			Integer upPresentNum, Integer channelId, Integer spreadTypeId,
			Integer districtId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("downCookieNum", downCookieNum);
		params.put("upCookieNum", upCookieNum);
		params.put("downIpNum", downIpNum);
		params.put("upIpNum", upIpNum);
		params.put("downFpNum", downFpNum);
		params.put("upFpNum", upFpNum);
		params.put("downDevIdNum", downDevIdNum);
		params.put("upDevIdNum", upDevIdNum);
		params.put("downSequnetCardNum", downSequnetCardNum);
		params.put("upSequnetCardNum", upSequnetCardNum);
		params.put("downCardNum", downCardNum);
		params.put("upCardNum", upCardNum);
		params.put("downPresentNum", downPresentNum);
		params.put("upPresentNum", upPresentNum);
		
		params.put("channelId", channelId);
		params.put("spreadTypeId", spreadTypeId);
		params.put("districtId", districtId);
		
		params.put("presentUserId", presentUserId);
		return userRegistMapper.countRegistUserAnalyseStat(params);
	}

}
