package com.qbao.search.engine.service;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadConfig;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import net.sf.json.JSONObject;
import redis.RedisConst;
import redis.RedisUtil;
import util.TokenUtils;
import vo.OpenApiPo;

/**
 * 对外openApiservice方法
 * @author fanyunlong
 *
 */
public class OpenApiService {
	
	public static final ESLogger logger = Loggers.getLogger(OpenApiService.class);

	
	private RedisUtil redisUtil = new RedisUtil();
	
    /**
     * token获取
     * @param apiPo
     * @return
     */
	public Object getToken(OpenApiPo apiPo) {
		String appid = apiPo.getAppid();
		String secret = apiPo.getSecret();
		// 根据appid与secret去用户表中查询用户
		Map<String, String> usersMap = LoadConfig.getUserMap();
		JSONObject ruturnJson = new JSONObject();
        // 校验用户生成token权限
		if (null == usersMap || StringUtils.isEmpty(usersMap.get(appid))) {
			ruturnJson.accumulate("errcode", 10001);
			ruturnJson.accumulate("errmsg", "invalid appid");
		} 
		String valueArg[] = usersMap.get(appid).split(":");
		String sec = valueArg[0];
		String userId = valueArg[1];
		if (!secret.equals(sec)) {
			ruturnJson.accumulate("errcode", 10001);
			ruturnJson.accumulate("errmsg", "invalid appid");
		} else {
			// 不存在直接return。存在生成新的token，刷新redis中的老token
			Integer expiredTime = Config.get().getInt("token.expired.time", 24);
			String token = TokenUtils.getInstance().generateToken(userId, (long)expiredTime);
			ruturnJson.accumulate("access_token", token);
			ruturnJson.accumulate("expires_in", expiredTime);
			logger.info("++++token++ " + token+"expires_in="+expiredTime);
			// token生成成功存入缓存中,累加访问次数
			String redisKey = RedisConst.TENGRONG_MS_TOKEN.key;
	        redisKey = String.format(redisKey, userId);
			redisUtil.set(redisKey, token, expiredTime * 60 * 60);
			String timesKey = RedisConst.TENGRONG_MS_TOKEN_TIMES.key;
			timesKey = String.format(timesKey, userId);
			String times = redisUtil.get(timesKey);
			int time = 1;
			if (StringUtils.isNotEmpty(times)) {
				time = Integer.parseInt(times);
				time = time + 1;
			}
			redisUtil.set(timesKey, time + "", RedisConst.TENGRONG_MS_TOKEN_TIMES.expired);
		}		
		return ruturnJson.toString();
	}

}
