package com.qianwang.service.confratio.impl;

import com.alibaba.fastjson.JSON;
import com.qianwang.constant.RedisConst;
import com.qianwang.dao.domain.advertisement.ConfRatio;
import com.qianwang.mapper.confratio.ConfRatioMapper;
import com.qianwang.service.confratio.ConfRatioService;
import com.qianwang.util.Redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/20
 */
@Service
public class ConfRatioServiceImpl implements ConfRatioService {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfRatioServiceImpl.class);

    @Autowired
    private ConfRatioMapper confRatioMapper;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public List<ConfRatio> getAll() {
        List<ConfRatio> ratios = JSON.parseArray(redisUtil.get(RedisConst.CREDIT_CONF_RATIO.key), ConfRatio.class);
        if (ratios != null && ratios.size() > 0) {
            return ratios;
        } else {
            ratios = confRatioMapper.getAll();
            redisUtil.set(RedisConst.CREDIT_CONF_RATIO.key, JSON.toJSONString(ratios));
        }
        return ratios;
    }

    @Override
    public int addConfRatio(List<ConfRatio> ratios, String userName) {
        delRedis();
        int flag = 0;
        try {
            if (ratios.size() > 0) {
                List<ConfRatio> list = getDis();
                if (list != null && list.size() > 0) {
                    for (ConfRatio confRatio : list) {
                        if (confRatio.getListName().equals(ratios.get(0).getListName())) {
                            return flag;
                        }
                    }
                }
                confRatioMapper.updateStatus();
                for (ConfRatio ratio : ratios) {
                    ratio.setOperator(userName);
                    ratio.setCreateTime(new Date());
                    ratio.setUpdateTime(new Date());
                    flag = confRatioMapper.insert(ratio);
                }
                redisUtil.set(RedisConst.CREDIT_CONF_RATIO.key, JSON.toJSONString(ratios));
            }
        } catch (Exception e) {
            LOG.error("ConfRatioServiceImpl-addConfRatio():e,{}", e);
            return flag;
        }
        return 1;
    }

    @Override
    public List<ConfRatio> getDis() {
        List<String> stats = confRatioMapper.getDis();
        List<ConfRatio> confRatios = null;
        if (stats.size() > 0) {
            confRatios = confRatioMapper.getStatus(stats);
        }
        return confRatios;
    }

    @Override
    public List<ConfRatio> getListBylistName(ConfRatio record) {
        return confRatioMapper.getListBylistName(record);
    }


    @Override
    public int upConfRatio(ConfRatio ratios, String userName) {
        delRedis();
        int flag = 0;
        try {
            List<ConfRatio> list = confRatioMapper.getListBylistName(ratios);
            if (list.size() > 0) {
                confRatioMapper.updateStatus();
                for (ConfRatio ratio : list) {
                    ratio.setStatus(false);
                    ratio.setUpdateTime(new Date());
                    ratio.setOperator(userName);
                    flag = confRatioMapper.updateByPrimaryKey(ratio);
                }
                redisUtil.set(RedisConst.CREDIT_CONF_RATIO.key, JSON.toJSONString(confRatioMapper.getListBylistName(ratios)));
            }
        } catch (Exception e) {
            LOG.error("ConfRatioServiceImpl-upConfRatio():e,{}", e);
            return flag;
        }
        return 1;
    }

    private void delRedis() {
        String key = String.format(RedisConst.CREDIT_CONF_RATIO_USER.key, "");
        redisUtil.batchDel(key);
    }
}
