package com.qianwang.service.confitem.impl;

import com.alibaba.fastjson.JSON;
import com.qianwang.constant.StatusEnum;
import com.qianwang.constant.RedisConst;
import com.qianwang.dao.domain.confitem.ConfItem;
import com.qianwang.dao.util.BeanUtils;
import com.qianwang.mapper.confitem.ConfItemMapper;
import com.qianwang.param.confitem.ItemPageParam;
import com.qianwang.service.confitem.ConfItemService;
import com.qianwang.service.dto.confitem.ItemPageDto;
import com.qianwang.service.vo.confitem.ItemQueryAllVO;
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
 * on 2017/11/16.
 */

@Service
public class ConfItemServiceImpl implements ConfItemService {
    protected static final Logger LOG = LoggerFactory.getLogger(ConfItemServiceImpl.class);
    @Resource
    private ConfItemMapper confItemMapper;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public ItemQueryAllVO selectAll(ItemPageDto dto) {
        ItemQueryAllVO itemQueryAllVO = null;
        try {
            itemQueryAllVO = new ItemQueryAllVO();
            ItemPageParam pageparam = BeanUtils.parse(dto, ItemPageParam.class);
            int count = confItemMapper.selectAllCount();

            List<ConfItem>  confItemList = confItemMapper.selectAll(pageparam);

            itemQueryAllVO.setItems(confItemList);
            itemQueryAllVO.setCount(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(),e);
        }
        return itemQueryAllVO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int insertConfItem(ConfItem confItem) {
        delRedis();
        int count = 0;
        try {
            confItem.setCreateTime(new Date());
            confItem.setUpdateTime(new Date());
            confItem.setStatus(StatusEnum.ONLINE.code);
            count = confItemMapper.insertSelective(confItem);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int updateByPrimaryKey(ConfItem confItem)  {
        delRedis();
        int count = 0;
        try {
            confItem.setUpdateTime(new Date());
            count =  confItemMapper.updateByPrimaryKeySelective(confItem);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return count;
    }

    private void  delRedis(){
        String  key = RedisConst.CREDIT_CONF_ITEM.key;
        String flagKey = String.format(RedisConst.CREDIT_CONF_ITEM_FLAG.key,"");
        redisUtil.del(key);
        redisUtil.batchDel(flagKey);
    }


    @Override
    public ConfItem queryByid(Integer id) {
        ConfItem confItem = confItemMapper.selectByPrimaryKey(id);
        return confItem;
    }



}
