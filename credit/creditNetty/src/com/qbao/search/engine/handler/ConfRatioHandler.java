package com.qbao.search.engine.handler;

import com.alibaba.fastjson.JSON;
import com.qbao.config.DbFactory;
import com.qbao.dao.ConfRatioMapper;
import com.qbao.dto.ConfRatio;
import com.qbao.search.engine.AjaxResult;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import redis.RedisConstant;
import redis.RedisUtil;

import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/21
 */
public class ConfRatioHandler  extends SimpleHttpRequestHandler<String> {
    @Override
    protected String doRun() throws Exception {
        RedisUtil redisUtil = RedisUtil.getInstance();

        RedisConstant key = RedisConstant.CREDIT_CONF_RATIO;

        String result = redisUtil.get(key.getKey());

        if (result == null) {

            ConfRatioMapper confRatioMapper = DbFactory.createBean(ConfRatioMapper.class);
            List<ConfRatio> list = confRatioMapper.getAll();
            result = JSON.toJSONString(AjaxResult.success(list));

            redisUtil.set(key.getKey(), result, key.getExpired());
        }

        return result;
    }

    @Override
    public void setServer(Server server) {

    }
}
