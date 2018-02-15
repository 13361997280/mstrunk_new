package com.qianwang.web.auth;

import com.qianwang.dao.domain.AuthoritiesResource;

import java.util.List;

/**
 * 权限处理
 *
 * @author song.j
 * @create 2017-08-22 10:10:53
 **/
public interface AuthHandler {

    void doCheck(Integer userId, List<AuthoritiesResource> list) throws AccessAuthException;
}
