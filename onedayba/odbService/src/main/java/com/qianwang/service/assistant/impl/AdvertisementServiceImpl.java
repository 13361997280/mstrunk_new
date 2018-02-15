package com.qianwang.service.assistant.impl;

import com.qianwang.dao.domain.advertisement.Advertisement;
import com.qianwang.mapper.advertisement.AdvertisementMapper;
import com.qianwang.service.assistant.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by songjie on 17/4/13.
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    protected static final Logger LOG = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return advertisementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(Advertisement record) {
        return advertisementMapper.insertSelective(record);
    }

    @Override
    public Advertisement selectByPrimaryKey(Integer id) {
        return advertisementMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Advertisement record) {
        return advertisementMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Advertisement> selectByCondi() {
        return advertisementMapper.selectByCondi();
    }

}
