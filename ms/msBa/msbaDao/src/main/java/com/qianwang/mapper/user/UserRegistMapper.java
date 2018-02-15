package com.qianwang.mapper.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.qianwang.dao.domain.user.UserAnalyseStat;
import com.qianwang.dao.domain.user.UserRegist;

public interface UserRegistMapper {

	List<UserRegist> selectUserRegistRecent(Date statDate);
	
	List<UserRegist> selectUserRegist(Map<String,Object> params);
	
	List<UserAnalyseStat> selectRegistUserAnalyseStat(Map<String,Object> params);
	Integer countRegistUserAnalyseStat(Map<String,Object> params);
}
