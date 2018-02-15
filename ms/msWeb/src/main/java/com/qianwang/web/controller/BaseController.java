package com.qianwang.web.controller;

import com.qianwang.service.properties.UrlProperties;
import com.qianwang.web.controller.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器基类
 *
 * @author zou
 */
@Component
public class BaseController extends UrlProperties {

    @Autowired
    HttpServletRequest request;

    @Autowired
    UrlProperties urlProperties;
    /**
     * 日志记录对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(CommonController.class);

    public Integer getCurrentUserId() throws RuntimeException {
        Object userId = request.getSession().getAttribute("userId");
        if (userId != null)
            return Integer.valueOf(userId.toString());

        if (userId == null)
            throw new UserException("请先登录！");

        return null;
    }

    public String getCurrentUserName() {
        Object userName = request.getSession().getAttribute("userName");
        if (userName != null)
            return userName.toString();

        return null;
    }

//    public boolean invokePass(){
//        if (urlProperties.messgaeUsers.contains(getCurrentUserName())){
//            return true;
//        }
//        return false;
//    }
}
