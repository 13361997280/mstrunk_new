package com.qianwang.service.user.impl;

import com.qianwang.dao.domain.UserSelectGroup;
import com.qianwang.dao.domain.user.Users;
import com.qianwang.mapper.UserSelectGroupMapper;
import com.qianwang.service.user.UserSelectGroupService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenghaijiang on 2017/5/22.
 */
@Service
public class UserSelectGroupServiceImpl implements UserSelectGroupService {
    protected static final Log LOG = LogFactory.getLog(UserSelectGroupServiceImpl.class);
    @Autowired
    private UserSelectGroupMapper mapper;
    @Override
    public List<UserSelectGroup> findAll(Integer userId) {
        return mapper.selectAll(userId);
    }
    @Override
    public Map getResultByPage(int page, int size, int userId) {
        Map pageMap = new HashMap();
        int count = mapper.selectByPageCount(userId);
        List dataList = mapper.selectByPage(page * size, size,userId);
        pageMap.put("count", count);
        pageMap.put("dataList", dataList);
        return pageMap;
    }
    @Override
    public void insert(UserSelectGroup entity) {
        mapper.insert(entity);
    }

    @Override
    public void update(UserSelectGroup entity) {
        mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public UserSelectGroup findOne(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

}
