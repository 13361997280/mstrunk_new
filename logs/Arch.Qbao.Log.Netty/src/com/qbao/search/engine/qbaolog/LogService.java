package com.qbao.search.engine.qbaolog;

import com.alibaba.fastjson.JSON;
import redis.RedisConst;
import redis.RedisUtil;

import java.util.Map;

/**
 * 日志数据服务
 *
 * @author song.j
 * @create 2017-07-11 15:15:37
 **/
public class LogService {
    public static RedisUtil redisUtil = new RedisUtil();
    public boolean saveLog(Map paramMap){

        redisUtil.rpush(RedisConst.TEGNRONG_ONEDAY_QBAOLOG_QUEUE.key, JSON.toJSONString(paramMap));

//        EsDataService.getInstance().saveQbaoLog(paramMap);
        return true;
    }
}
