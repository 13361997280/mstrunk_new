package com.qianwang.service.confbaseset.impl;

import com.alibaba.fastjson.JSON;
import com.qianwang.constant.RedisConst;
import com.qianwang.constant.StatusEnum;
import com.qianwang.dao.domain.confbaseset.ConfBaseSet;
import com.qianwang.dao.util.BeanUtils;
import com.qianwang.mapper.confbaseset.ConfBaseSetMapper;
import com.qianwang.param.confbaseset.BaseSetParam;
import com.qianwang.service.confbaseset.ConfBaseSetService;
import com.qianwang.service.dto.confbaseset.BaseSetPageDto;
import com.qianwang.service.vo.confbaseset.BaseSetQueryAllVO;
import com.qianwang.util.Redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by fun
 * on 2017/11/17.
 */
@Service
public class ConfBaseSetServiceImpl implements ConfBaseSetService {
    protected static final Logger LOG = LoggerFactory.getLogger(ConfBaseSetServiceImpl.class);
    @Resource
    private ConfBaseSetMapper confBaseSetMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public  BaseSetQueryAllVO selectAll(BaseSetPageDto dto) {
        BaseSetQueryAllVO baseSetQueryAllVO = null;
        try {
            baseSetQueryAllVO = new BaseSetQueryAllVO();
            BaseSetParam pageparam = BeanUtils.parse(dto, BaseSetParam.class);

            ConfBaseSet confBaseSet = confBaseSetMapper.selectByOnline();
            baseSetQueryAllVO.setConfBaseSet(confBaseSet);

            List<ConfBaseSet> confBaseSetList =  confBaseSetMapper.selectByOffline(pageparam);
            int count = confBaseSetMapper.selectByOfflineCount();
            baseSetQueryAllVO.setItems(confBaseSetList);
            baseSetQueryAllVO.setCount(count);
            return baseSetQueryAllVO;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(),e);
        }
        return baseSetQueryAllVO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int save(ConfBaseSet confBaseSet)  {
        int count =0;
        String  key = String.format(RedisConst.CREDIT_CONF_BASE_SET.key);
        ConfBaseSet confBaseSetOnle = confBaseSetMapper.selectByOnline();
        try {
            if( null != confBaseSetOnle){
                ConfBaseSet baseSet = new ConfBaseSet();
                baseSet.setStatus(StatusEnum.OFFLINE.code);
                baseSet.setUpdateTime(new Date());
                baseSet.setId(confBaseSetOnle.getId());
                confBaseSetMapper.updateByPrimaryKeySelective(baseSet);
            }

            confBaseSet.setCreateTime(new Date());
            confBaseSet.setUpdateTime(new Date());
            confBaseSet.setStatus(StatusEnum.ONLINE.code);
            count = confBaseSetMapper.insertSelective(confBaseSet);

            redisUtil.set(key,JSON.toJSONString(confBaseSet),RedisConst.CREDIT_CONF_BASE_SET.expired);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return count;
    }


}
