package com.qianwang.service.assistant.impl;

import com.qianwang.dao.domain.assistant.AuthoritiesResource;
import com.qianwang.mapper.assistant.AuthoritiesResourceMapper;
import com.qianwang.service.assistant.AuthoritiesResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by songjie on 16/10/12.
 */
@Service
public class AuthoritiesResourceServiceImpl implements AuthoritiesResourceService {

    @Autowired
    AuthoritiesResourceMapper mapper;

    @Override
    public Boolean add(AuthoritiesResource entity) {
        return mapper.insertSelective(entity) >= 1;
    }

    @Override
    public List<AuthoritiesResource> getList() {
        return mapper.selectList();
    }

    @Override
    public int getCount() {
        return mapper.selectCount();
    }

    @Override
    public Boolean edit(AuthoritiesResource entity) {
        return mapper.updateByPrimaryKeySelective(entity) >= 1;
    }

    @Override
    public Boolean delete(Integer id) {
        return mapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public AuthoritiesResource getEntity(Integer id){
        return mapper.selectByPrimaryKey(id);
    }
    
}
