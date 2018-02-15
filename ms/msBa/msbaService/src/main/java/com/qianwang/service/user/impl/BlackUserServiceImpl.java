package com.qianwang.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qianwang.dao.domain.user.UnusualUser;
import com.qianwang.util.lang.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianwang.dao.aspect.RecentData;
import com.qianwang.dao.util.DataSourceContextHolder;
import com.qianwang.mapper.user.BusiUserLoginBlackListMapper;
import com.qianwang.service.user.BlackUserService;

@Service
public class BlackUserServiceImpl implements BlackUserService {
    protected static final Log LOG = LogFactory.getLog(BlackUserServiceImpl.class);

    @Autowired
    private BusiUserLoginBlackListMapper busiUserLoginBlackListMapper;

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_65)
    public List<Map<String, Object>> getBlackUser(String startDate, String endDate, String userName, Integer lockReasonType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("userName", userName);
        params.put("lockReasonType", lockReasonType);
        return busiUserLoginBlackListMapper.getBlackUser(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_65)
    public List<Map<String, Object>> getBlackUserDayGroup(String startDate, String endDate, String userName,
                                                          Integer lockReasonType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("userName", userName);
        params.put("lockReasonType", lockReasonType);
        return busiUserLoginBlackListMapper.getBlackUserDayGroup(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_65)
    public List<Map<String, Object>> getBlackUserHourGroup(String startDate, String endDate, String userName,
                                                           Integer lockReasonType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("userName", userName);
        params.put("lockReasonType", lockReasonType);
        return busiUserLoginBlackListMapper.getBlackUserHourGroup(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_65)
    public List<Map<String, Object>> getBlackUserReason() {
        return busiUserLoginBlackListMapper.getBlackUserReason();
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_14_64_3306)
    public List<Map<String, Object>> getUnBlackUser(String startDate, String endDate, String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("userName", userName);
        return busiUserLoginBlackListMapper.getUnBlackUser(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_14_64_3306)
    public List<Map<String, Object>> getUnBlackUserDayGroup(String startDate, String endDate, String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("userName", userName);
        return busiUserLoginBlackListMapper.getUnBlackUserDayGroup(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_14_64_3306)
    public List<Map<String, Object>> getUnBlackUserHourGroup(String startDate, String endDate, String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("userName", userName);
        return busiUserLoginBlackListMapper.getUnBlackUserHourGroup(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_14_64_3306)
    public List<Map<String, Object>> getUnBlackUserByTime(String time) {
        Map<String, Object> params = new HashMap<String, Object>();
        String startDate = "";
        String endDate = "";
        try {
            startDate = DateUtil.formatDate(DateUtil.parseDate(time, DateUtil.TIMESTAMP_PATTERN), "yyyy-MM-dd HH") + ":00:00";
            endDate = DateUtil.formatDate(DateUtil.dateCompute(DateUtil.parseDate(time, DateUtil.TIMESTAMP_PATTERN), DateUtil.DATE_COMPUTE_TYPE_HOUR, 1), "yyyy-MM-dd HH") + ":00:00";
        } catch (Exception e) {
        }
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return busiUserLoginBlackListMapper.getUnBlackUserByTime(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_14_64_3306)
    public List<Map<String, Object>> getUnBlackUserByDay(String time) {
        Map<String, Object> params = new HashMap<String, Object>();
        String startDate = "";
        String endDate = "";
        try {
            startDate = DateUtil.formatDate(DateUtil.parseDate(time, DateUtil.SHORT_TIMESTAMP_PATTERN), DateUtil.SHORT_TIMESTAMP_PATTERN) + " 00:00:00";
            endDate = DateUtil.formatDate(DateUtil.dateCompute(DateUtil.parseDate(time, DateUtil.SHORT_TIMESTAMP_PATTERN), DateUtil.DATE_COMPUTE_TYPE_DAY, 1), DateUtil.SHORT_TIMESTAMP_PATTERN) + " 00:00:00";
        } catch (Exception e) {
        }
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return busiUserLoginBlackListMapper.getUnBlackUserByDay(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_65)
    public List<Map<String, Object>> getYiChangUser(String startDate, String endDate, String userName, Integer lockReasonType) {
        Map<String, Object> params = new HashMap<String, Object>();
        return busiUserLoginBlackListMapper.getYiChangUser(params);
    }

    @Override
    @RecentData(dataSourceName = DataSourceContextHolder.DW_65)
    public int updateYiChangUser(String orderId, Integer type) {
        UnusualUser unusualUser = new UnusualUser();
        Map<String, Object> params = new HashMap<String, Object>();
        unusualUser.setOrderId(orderId);
        unusualUser.setDealResult(type);
        return busiUserLoginBlackListMapper.updateYiChangUser(unusualUser);
    }
}
