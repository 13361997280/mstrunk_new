package com.qianwang.service.assistant.impl;

import com.qianwang.dao.aspect.DataSource;
import com.qianwang.dao.aspect.DbMsEnum;
import com.qianwang.dao.domain.assistant.Authorities;
import com.qianwang.dao.domain.assistant.AuthoritiesGroup;
import com.qianwang.dao.domain.assistant.AuthoritiesResource;
import com.qianwang.mapper.assistant.AuthoritiesGroupMapper;
import com.qianwang.mapper.assistant.AuthoritiesMapper;
import com.qianwang.mapper.assistant.AuthoritiesResourceMapper;
import com.qianwang.service.assistant.AuthoritiesGroupService;
import com.qianwang.service.assistant.AuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by songjie on 16/10/12.
 */
@Service
public class AuthoritiesGroupServiceImpl implements AuthoritiesGroupService {

    @Autowired
    AuthoritiesGroupMapper mapper;
    @Autowired
    AuthoritiesResourceMapper resourceMapper;

    @Override
    public Boolean add(AuthoritiesGroup entity) {
        return mapper.insertSelective(entity) >= 1;
    }

    @Override
    public List<AuthoritiesGroup> getList() {
        List<AuthoritiesGroup> list = mapper.selectList();
        for(AuthoritiesGroup entity:list){
            String resourceIds = entity.getResouceIds();
            String[] resourceIdsArray = resourceIds.split(",");
            String names = "";
            for(String id : resourceIdsArray){
                AuthoritiesResource resource = resourceMapper.selectByPrimaryKey(Integer.parseInt(id));
                if(resource!=null) {
                    names = names + resource.getName() + ",";
                }
            }
            if(names.length()>0) {
                entity.setResouceNames(names.substring(0, names.length() - 1));
            }
        }
        return list;
    }

    @Override
    public int getCount() {
        return mapper.selectCount();
    }

    @Override
    public Boolean edit(AuthoritiesGroup entity) {
        return mapper.updateByPrimaryKeySelective(entity) >= 1;
    }

    @Override
    public Boolean delete(Integer id) {
        return mapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public AuthoritiesGroup getEntity(Integer id){
        return mapper.selectByPrimaryKey(id);
    }
    
}
