package com.qianwang.web.auth;

/**
 * @author song.j
 * @create 2017-08-22 10:10:56
 **/
public class AuthRoute {

    private AuthHandler authHandler;

    public AuthRoute(MsAuth msAuth, String uri) {

        if (msAuth.role().equals(CheckEnum.CHECK)) {
            authHandler = new AccessAuthHandler(msAuth, uri);  //访问权限处理器
        }
    }

    public AuthHandler getAuthHandler() throws IllegalAccessException {
        if (authHandler == null){
            throw new IllegalAccessException("authhandler not be null");
        }

        return authHandler;
    }
}
