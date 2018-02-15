package com.qbao.search.engine.handler;

import com.alibaba.fastjson.JSON;
import com.qbao.config.DbFactory;
import com.qbao.dao.ConfBaseSetMapper;
import com.qbao.dto.ConfBaseSet;
import com.qbao.search.engine.AjaxResult;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import redis.RedisConstant;
import redis.RedisUtil;


/**
 * Created by fun
 * on 2017/11/20.
 */
public class BaseSetHandler extends SimpleHttpRequestHandler<String> {

    @Override
    protected String doRun() throws Exception {
        RedisUtil redisUtil = RedisUtil.getInstance();
        RedisConstant key = RedisConstant.CREDIT_CONF_BASE_SET;
        String result = redisUtil.get(key.getKey());
        if (result == null) {
            ConfBaseSetMapper confBaseSetMapper = DbFactory.createBean(ConfBaseSetMapper.class);
            ConfBaseSet  confBaseSet= confBaseSetMapper.selectByOnline();
            result = JSON.toJSONString(AjaxResult.success(confBaseSet));
            redisUtil.set(key.getKey(), result, key.getExpired());
        }
        return result;
    }

    @Override
    public void setServer(Server server) {

    }


}
