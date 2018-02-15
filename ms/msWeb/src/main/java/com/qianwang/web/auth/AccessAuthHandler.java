package com.qianwang.web.auth;

import com.qianwang.dao.domain.AuthoritiesResource;

import java.util.List;

/**
 * 普通的权限检查。是否拥有访问权限
 *
 * @author song.j
 * @create 2017-08-22 10:10:58
 **/
public class AccessAuthHandler implements AuthHandler {

    private MsAuth msAuth;
    private String uri;

    public AccessAuthHandler(MsAuth msAuth, String uri) {
        this.msAuth = msAuth;
        this.uri = uri;
    }

    @Override
    public void doCheck(Integer userId, List<AuthoritiesResource> list) throws AccessAuthException {
        boolean flag = true;
        for (AuthoritiesResource resource : list) {
            if (resource.getValue().equals(uri)) {
                flag = false;
            }
        }
        if (flag)
            throw new AccessAuthException("权限不足,请联系管理员");

    }
}
