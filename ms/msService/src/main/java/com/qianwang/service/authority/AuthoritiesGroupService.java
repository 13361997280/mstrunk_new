package com.qianwang.service.authority;

import com.qianwang.dao.domain.AuthoritiesResource;

import java.util.List;

/**
 * @author song.j
 * @create 2017-08-23 10:10:36
 **/
public interface AuthoritiesGroupService {
    List<AuthoritiesResource> getAuthByUserId(Integer userId);
}
