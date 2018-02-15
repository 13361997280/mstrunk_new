package com.qbao.search.engine.handler;

import com.alibaba.fastjson.JSON;
import com.qbao.config.DbFactory;
import com.qbao.dao.ConfItemMapper;
import com.qbao.dto.ConfItem;
import com.qbao.search.engine.AjaxResult;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import redis.RedisConstant;
import redis.RedisUtil;

import java.util.List;

/**
 * @author song.j
 * @create 2017-11-16 09:09:18
 **/
public class BusItemHandler extends SimpleHttpRequestHandler<String> {
    @Override
    protected String doRun() throws Exception {
        RedisUtil  redisUtil = RedisUtil.getInstance();

        RedisConstant key = RedisConstant.CREDIT_CONF_ITEM;

        String result = redisUtil.get(key.getKey());

        if (result == null) {

            ConfItemMapper confItemMapper = DbFactory.createBean(ConfItemMapper.class);
            List<ConfItem> list = confItemMapper.selectUse();
            result = JSON.toJSONString(list);

            redisUtil.set(key.getKey(), result, key.getExpired());
        }

        return AjaxResult.success(JSON.parseArray(result,ConfItem.class)).toString();
    }

    @Override
    public void setServer(Server server) {

    }
}
