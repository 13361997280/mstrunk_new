package com.qianwang.service.user.impl;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianwang.dao.aspect.RecentData;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.user.UserAuthority;
import com.qianwang.dao.domain.user.UserRegist;
import com.qianwang.dao.domain.user.UserView;
import com.qianwang.dao.util.DataSourceContextHolder;
import com.qianwang.mapper.user.HeavyCUserStatMapper;
import com.qianwang.mapper.user.HeavyPUserStatMapper;
import com.qianwang.mapper.user.LightFinancialStatRealMapper;
import com.qianwang.mapper.user.LightUserChannelActionStatDayMapper;
import com.qianwang.mapper.user.UserMapper;
import com.qianwang.mapper.user.UserRegistMapper;
import com.qianwang.service.user.UserService;

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
	
	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	public List<UserRegist> selectUserRegist(Date startDate,Date endDate){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return userRegistMapper.selectUserRegist(params);
	}
	
	
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private LightFinancialStatRealMapper lightFinancialStatRealMapper;

	/**
	 * 查询所有
	 * 
	 * @return 用户信息集合
	 */
	@Override
	public List<UserView> findAllUser() {
		return userMapper.selectAllUser();
	}

	/**
	 * 插入用户
	 * 
	 * @param user
	 */
	@Override
	public void insertUser(UserView user) {
		userMapper.insertUser(user);
	}

	/**
	 * 根据用户名加载用户
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public UserView loadUser(String username) {
		return userMapper.loadUser(username);
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 */
	@Override
	public void updateUser(UserView user) {
		userMapper.updateUser(user);
	}

	/**
	 * 根据用户名删除用户
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public void deleteUser(String username) {
		userMapper.deleteUser(username);
	}
	
	/**
	 * 配置用户权限信息
	 * @param username 用户名
	 * @param authorities 逗号分隔权限集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void configUser(String username,String authorities){
		userMapper.deleteUserAuthority(username);
		if(StringUtils.isNotEmpty(authorities)){
			String[] authorityArray = authorities.split(",");
			for (String authority : authorityArray) {
				UserAuthority userAuthority = new UserAuthority();
				userAuthority.setUsername(username);
				userAuthority.setAuthorityCode(authority);
				userMapper.addUserAuthority(userAuthority);
			}
		}
	}
	
	/**
	 * 根据用户名查询权限菜单
	 * @param username
	 * @param parentId
	 * @return
	 */
	@Override
	public List<Menu> findUserMenu(String username,int parentId){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("username", username);
		param.put("parentId", parentId);
		return userMapper.findUserMenu(param);
	}
	@Override
	public List<Map<String, Object>> getChangdi() {
		return heavyPUserStatMapper.getChangdi();
	}
	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_14_63_3306)
	public List<Map<String, Object>> getSignInfo(String startDate,
			String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getSignInfo(params);
	}
	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getClassifySignInfo(String startDate,
			String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getClassifySignInfo(params);
	}
	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getSignHourInfo(String startDate,
			String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getSignHourInfo(params);
	}
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getMarketIndexInfo(String startDate,
			String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightFinancialStatRealMapper.getMarketIndexInfo(params);
	}
	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_14_64_3306)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> getUserSourceRec(String startDate,
			String endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return lightUserChannelActionStatDayMapper.getUserSourceRec(params);
	}
	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_14_63_3306)
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
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
	public List<Map<String, Object>> findLastRegistUserRecent() {
		return lightUserChannelActionStatDayMapper.findLastRegistUserRecent();
	}

	@Override
	public boolean hasModifyPass(String currentUsername) {
		UserView user = userMapper.loadUser(currentUsername);
		return user.isModifyPwd();
	}

	@Override
	public boolean hasBindMobile(String currentUsername) {
		UserView user = userMapper.loadUser(currentUsername);
		return user.isBindMobile();
	}

	@Override
	public void bindMobile(UserView user) {
		userMapper.bindMobile(user);
	}

	@Override
	@RecentData(dataSourceName=DataSourceContextHolder.DW_65)
	public List<Map<String, String>> loadUserQbAccountInfo(String mobile) {
		return userMapper.loadUserQbAccountInfo(mobile);
	}
}
