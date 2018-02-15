package com.qianwang.service.user.impl;

import com.qianwang.dao.aspect.RecentData;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.user.UserAuthority;
import com.qianwang.dao.domain.user.UserRegist;
import com.qianwang.dao.domain.user.Users;
import com.qianwang.dao.util.DataSourceContextHolder;
import com.qianwang.mapper.user.*;
import com.qianwang.service.user.UserService;
import com.qianwang.util.security.DcPasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
	protected static final Log LOG = LogFactory.getLog(UserServiceImpl.class);

	@Autowired
	private LightUserChannelActionStatDayMapper lightUserChannelActionStatDayMapper;

	@Autowired
	private HeavyPUserStatMapper heavyPUserStatMapper;

	@Autowired
	private HeavyCUserStatMapper heavyCUserStatMapper;

	@Autowired
	private UserRegistMapper userRegistMapper;
	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private LightFinancialStatRealMapper lightFinancialStatRealMapper;


	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSource(String startDate,
												   String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("registerChannel", registerChannel);
		params.put("spreadType", spreadType);
		params.put("registerArea", registerArea);
		params.put("tuijian", tuijian);
		return lightUserChannelActionStatDayMapper.getUserSource(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSourceHour(String startDate,
													   String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("registerChannel", registerChannel);
		params.put("spreadType", spreadType);
		params.put("registerArea", registerArea);
		params.put("tuijian", tuijian);
		return lightUserChannelActionStatDayMapper.getUserSourceHour(params);
	}

	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSourceGroup(String startDate,
														String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("registerChannel", registerChannel);
		params.put("spreadType", spreadType);
		params.put("registerArea", registerArea);
		params.put("tuijian", tuijian);
		return lightUserChannelActionStatDayMapper.getUserSourceGroup(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSourceHourGroup(String startDate,
															String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("registerChannel", registerChannel);
		params.put("spreadType", spreadType);
		params.put("registerArea", registerArea);
		params.put("tuijian", tuijian);
		return lightUserChannelActionStatDayMapper.getUserSourceHourGroup(params);
	}
	@Override
	public List<Map<String, Object>> getPUser(int presenterId, String sort, int start, int length) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("presenterId", presenterId);
		params.put("sort", sort);
		params.put("start", start);
		params.put("length", length);
		return heavyPUserStatMapper.getPUser(params);
	}
	@Override
	public int getPUserCnt(int presenterId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("presenterId", presenterId);
		return heavyPUserStatMapper.getPUserCnt(params);
	}
	@Override
	public List<Map<String, Object>> getCUser(int presenterId, String sort, int start, int length) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("presenterId", presenterId);
		params.put("sort", sort);
		params.put("start", start);
		params.put("length", length);
		return heavyCUserStatMapper.getCUser(params);
	}
	@Override
	public int getCUserCnt(int presenterId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("presenterId", presenterId);
		return heavyCUserStatMapper.getCUserCnt(params);
	}

	@Override
	public List<Map<String, Object>> getTicketBee(String startDate, String endDate, String xiaomifeng, String changdi, String sort, int start, int length) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("xiaomifeng", xiaomifeng);
		params.put("changdi", changdi);
		params.put("sort", sort);
		params.put("start", start);
		params.put("length", length);
		return heavyPUserStatMapper.getTicketBee(params);
	}
	@Override
	public int getTicketBeeCnt(String startDate, String endDate, String xiaomifeng, String changdi) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("xiaomifeng", xiaomifeng);
		params.put("changdi", changdi);
		return heavyPUserStatMapper.getTicketBeeCnt(params);
	}

	@Override
	public List<UserRegist> selectUserRegistRecent(Date statDate){
		return userRegistMapper.selectUserRegistRecent(statDate);
	}

	@Override
	public List<UserRegist> selectUserRegist(Date startDate, Date endDate){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return userRegistMapper.selectUserRegist(params);
	}





	@Override
	public List<Users> findAllUser() {
		return null;
	}

	/**
	 * 插入用户
	 *
	 * @param user
	 */
	@Override
	public Integer insertUser(Users user) {
		return usersMapper.insert(user);
	}

	@Override
	public void updateUser(Users user) {

	}

	@Override
	public Users loadUser(String username) {
		return usersMapper.loadUser(username);
	}

	@Override
	public void deleteUser(String username) {

	}

	@Override
	public void configUser(String username, String authorities) {

	}

	@Override
	public List<Menu> findUserMenu(String username, int parentId) {
		return null;
	}


	@Override
	public List<Map<String, Object>> getChangdi() {
		return heavyPUserStatMapper.getChangdi();
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_14_63_3306)
	public List<Map<String, Object>> getSignInfo(String startDate,
												 String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getSignInfo(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getClassifySignInfo(String startDate,
														 String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getClassifySignInfo(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getSignHourInfo(String startDate,
													 String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getSignHourInfo(params);
	}
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getClassifySignHourInfo(String startDate,
															 String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getClassifySignHourInfo(params);
	}
	@Override
	public Map<String, Object> getXiaomifengByName(String xiaomifeng) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("xiaomifeng", xiaomifeng);
		return heavyPUserStatMapper.getXiaomifengByName(params);
	}
	@Override
	public Map<String, Object> getXiaomifengUserByName(String xiaomifeng) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("xiaomifeng", xiaomifeng);
		return heavyPUserStatMapper.getXiaomifengUserByName(params);
	}

	@Override
	public void saveXiaomifeng(String xiaomifeng, String changdi, String username) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("xiaomifeng", xiaomifeng);
		params.put("changdi", changdi);
		LOG.info("用户[" + username + "]新增小蜜蜂[小蜜蜂钱宝用户名:"
				+ xiaomifeng + ", 场地:"
				+ changdi + "]");
		heavyPUserStatMapper.saveXiaomifeng(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getMarketIndexInfo(String startDate,
														String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightFinancialStatRealMapper.getMarketIndexInfo(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getMarketIndexRealInfo(String startDate,
															String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightFinancialStatRealMapper.getMarketIndexRealInfo(params);
	}
	@Override
	public List<Map<String, Object>> getAssetsInfo(String startDate,
												   String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightFinancialStatRealMapper.getAssetsInfo(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_14_64_3306)
	public List<Map<String, Object>> getAssetsRealInfo(String startDate,
													   String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightFinancialStatRealMapper.getAssetsRealInfo(params);
	}
	@Override
	public Map<String, Object> getUserIdByName(String username) {
		return heavyPUserStatMapper.getUserIdByName(username);
	}
	@Override
	public List<Map<String, Object>> getCUsersByPresentId(int presenterId) {
		return heavyCUserStatMapper.getCUserByPresentId(presenterId);
	}
	@Override
	public List<Map<String, Object>> getUserRechargeDist(String startDate,
														 String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getUserRechargeDist(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSourceRec(String startDate,
													  String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getUserSourceRec(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSourceRecHour(String startDate,
														  String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getUserSourceRecHour(params);
	}
	@Override
	public List<Map<String, Object>> getAllUserTaskActivity(String startDate,
															String endDate, String startAssets, String endAssets, String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("startAssets", startAssets);
		params.put("endAssets", endAssets);
		params.put("type", type);
		if(StringUtils.isEmpty(type) || "-1".equals(type)){
			return lightUserChannelActionStatDayMapper.getAllUserTaskActivityCustom(params);
		}
		return lightUserChannelActionStatDayMapper.getAllUserTaskActivity(params);
	}
	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_14_63_3306)
	public List<Map<String, Object>> getUserTaskActivityTblInfo(
			String startDate, String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getUserTaskActivityTblInfo(params);
	}

	@Override
	public List<Map<String, Object>> findUserInfoByMobile(String mobile) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile", mobile);
		return lightUserChannelActionStatDayMapper.findUserInfoByMobile(params);
	}

	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> findLastRegistUserRecent() {
		return lightUserChannelActionStatDayMapper.findLastRegistUserRecent();
	}




}
