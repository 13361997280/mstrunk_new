package com.qianwang.service.quartz.impl;

import com.qianwang.dao.domain.QuartTask;
import com.qianwang.mapper.QuartTaskMapper;
import com.qianwang.service.quartz.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author song.j
 * @create 2017-05-23 10:10:58
 **/
@Service
public class TaskServiceImpl implements TaskService {


    @Autowired
    QuartTaskMapper quartTaskMapper;

    @Override
    public List findTask() {
        return quartTaskMapper.selectAll();
    }
}
