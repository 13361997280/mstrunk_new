package com.qianwang.web.auth;

import com.qianwang.dao.domain.AuthoritiesResource;
import com.qianwang.service.authority.AuthoritiesGroupService;
import com.qianwang.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口拦截器
 *
 * @author song.j
 * @create 2017-08-22 09:09:49
 **/
@Aspect
@Component
public class MsInterfaceAspect {


    private static final Logger log = LoggerFactory.getLogger(MsInterfaceAspect.class);

    @Autowired
    HttpServletRequest request;

    @Autowired
    BaseController baseController;

    @Autowired
    AuthoritiesGroupService authoritiesGroupService;

    @Before(value = "@annotation(msAuth)", argNames = "msAuth")
    public void doBefore(MsAuth msAuth) throws IllegalAccessException, AccessAuthException {

        log.info("MsInterfaceAspect-doBefore url = {},msAuth = {}", request.getRequestURI(), msAuth.role().toString());

        // check uri
        String uri = StringUtils.isNotEmpty(msAuth.url()) ? msAuth.url() : request.getRequestURI();

        //当前登录用户ID
        Integer userId = baseController.getCurrentUserId();

        //权限路由。
        AuthRoute authRoute = new AuthRoute(msAuth, uri);

        //用户所拥有的权限列表
        List<AuthoritiesResource> list = authoritiesGroupService.getAuthByUserId(userId);

        authRoute.getAuthHandler().doCheck(userId, list);
    }
}
