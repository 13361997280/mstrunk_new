package com.qianwang.service.marketing.impl;

import com.qianwang.dao.domain.ActSend;
import com.qianwang.mapper.ActSendMapper;
import com.qianwang.service.marketing.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author song.j
 * @create 2017-05-16 11:11:34
 **/
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActSendMapper actSendMapper;

    @Override
    public Map getActivityByPage(int page, int size,int userId) {
        Map pageMap = new HashMap();
        int count = actSendMapper.selectByPageCount(userId);
        List dataList = actSendMapper.selectByPage(page * size, size,userId);
        pageMap.put("count", count);
        pageMap.put("dataList", dataList);
        return pageMap;
    }

    @Override
    public int saveActivity(ActSend actSend) {
        actSendMapper.insertSelective(actSend);
        return actSend.getId();
    }

}
