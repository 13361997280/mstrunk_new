package com.qianwang.service.assistant.impl;

import com.qianwang.dao.domain.assistant.Users;
import com.qianwang.mapper.assistant.UsersMapper;
import com.qianwang.mapper.assistant.AuthoritiesResourceMapper;
import com.qianwang.service.assistant.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by songjie on 16/10/12.
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersMapper mapper;

    @Override
    public Boolean add(Users entity) {
        return mapper.insertSelective(entity) >= 1;
    }

    @Override
    public List<Users> getList(Integer start, Integer size) {
        return mapper.selectList(start,size);
    }

    @Override
    public List<Users> getListForAuth(Integer id) {
        return mapper.selectListForAuth(id);
    }

    @Override
    public int getCount() {
        return mapper.selectCount();
    }

    @Override
    public int existUserName(String username) {
        return mapper.existUserName(username);
    }
    @Override
    public Boolean edit(Users entity) {
        return mapper.updateByPrimaryKeySelective(entity) >= 1;
    }

    @Override
    public Boolean delete(Integer id) {
        return mapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Users getEntity(Integer id){
        return mapper.selectByPrimaryKey(id);
    }
    
}
