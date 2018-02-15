package com.qianwang.service.authority.impl;

import com.qianwang.dao.domain.AuthoritiesResource;
import com.qianwang.mapper.AuthoritiesGroupMapper;
import com.qianwang.mapper.AuthoritiesResourceMapper;
import com.qianwang.service.authority.AuthoritiesGroupService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author song.j
 * @create 2017-08-23 10:10:37
 **/
@Service
public class AuthoritiesGroupServiceImpl implements AuthoritiesGroupService {

    @Autowired
    AuthoritiesResourceMapper authoritiesResourceMapper;

    @Autowired
    AuthoritiesGroupMapper authoritiesGroupMapper;

    public List<AuthoritiesResource> getAuthByUserId(Integer userId){

        List<AuthoritiesResource>  authList = new ArrayList();

        String resouce = authoritiesGroupMapper.selectResouceIdByUserId(userId);

        if (StringUtils.isEmpty(resouce))
            return authList;


        String[] resouces = resouce.split(",");


        authList = authoritiesResourceMapper.selectByIds(resouces);

        return authList;
    }
}
