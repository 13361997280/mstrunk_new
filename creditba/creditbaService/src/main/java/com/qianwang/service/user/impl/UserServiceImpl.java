package com.qianwang.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.qb.loginapi.dubbo.service.UserRouteDubboService;
import com.qb.ucslaveapi.dubbo.service.UserDubboService;
import com.qb.user.common.entity.UserRoute;
import com.qianwang.service.user.UserService;
import com.qianwang.util.HttpUtils;
import com.qianwang.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserRouteDubboService userRouteDubboService;
    @Resource
    private UserDubboService userDubboService;

    @Value("${uc.duboo.salt}")
    private String ucDubboSalt;
    @Value("${uc.duboo.appid}")
    private String ucDubboAppId;

    private final String queryUserRoleByUserIdUrl="http://api.user.qbao.com/api/get/userRole/%s/new";

    @Override
    public Map<String, Object> getUserFromHyip(String userName) {
        String sources = "username=" + userName + "&securityKey=" + ucDubboSalt;
        String json = null;
        logger.info("用户中心查询用户信息:{}", sources);
        try {
            String md5Str = Md5Utils.encryptionStr(userName + ucDubboSalt);
            UserRoute userRoute = userRouteDubboService.queryUserRouteByAccount(md5Str, ucDubboAppId, userName);
            if (userRoute == null) {
                return null;
            }
            md5Str = Md5Utils.encryptionStr(userRoute.getId() + ucDubboSalt);
            json = userDubboService.loadUserDetailView(md5Str, ucDubboAppId, userRoute.getId());
            logger.info("获取用户信息返回:{}", json);
        } catch (Exception e) {
            logger.error("获取用户信息出错:username-" + userName + ",错误:" + e.getMessage());
            e.printStackTrace();
        }
        if (json == null || json.trim().length() == 0) {
            return null;
        }
        Map<String, Object> map = JSON.<Map<String, Object>>parseObject(json, Map.class);
        return map;
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        List<String> list = new ArrayList<String>();
        logger.info("获取用户角色:请求参数：{}", userId);
        try {
            String result = HttpUtils.get(String.format(queryUserRoleByUserIdUrl, userId));
            logger.info("根据用户ID获取用户角色接口, 返回:{}", result);
            list = JSON.parseArray(result, String.class);
        } catch (Exception e) {
            logger.error("根据用户ID获取用户角色接口,错误：{}", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return list;
    }
}
