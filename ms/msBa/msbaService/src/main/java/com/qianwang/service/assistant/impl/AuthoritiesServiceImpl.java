package com.qianwang.service.assistant.impl;

import com.qianwang.dao.domain.assistant.Authorities;
import com.qianwang.mapper.assistant.AuthoritiesMapper;
import com.qianwang.service.assistant.AuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by songjie on 16/10/12.
 */
@Service
public class AuthoritiesServiceImpl implements AuthoritiesService {

    @Autowired
    AuthoritiesMapper mapper;

    @Override
    public Boolean addAuthorities(Authorities promotion) {
        return mapper.insertSelective(promotion) >= 1;
    }

    @Override
    public List<Authorities> getAuthorities() {
        return mapper.selectAuthorities();
    }

    @Override
    public int getAuthoritiesCount() {
        return mapper.selectAuthoritiesCount();
    }

    @Override
    public Boolean editAuthorities(Authorities entity) {
        return mapper.updateByPrimaryKeySelective(entity) >= 1;
    }

    @Override
    public Boolean removeAuthorities(Integer entityId) {
        return mapper.deleteByPrimaryKey(entityId) == 1;
    }

    @Override
    public Authorities getAuthorities(Integer id){
        return mapper.selectByPrimaryKey(id);
    }
    
}
