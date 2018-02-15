package com.qianwang.web.controller.exception;

import com.qianwang.web.auth.AccessAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 服务器全局异常管理
 *
 * @author song.j
 * @create 2017-05-27 11:11:22
 **/
@Component
public class MsExceptionHandler implements HandlerExceptionResolver {

    private final static Logger LOG = LoggerFactory.getLogger(MsExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) {

        ModelAndView mv = new ModelAndView();
            /*	使用response返回	*/
        response.setStatus(HttpStatus.OK.value()); //设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
        response.setCharacterEncoding("UTF-8"); //避免乱码
        response.setHeader("Cache-Control", "no-cache, must-revalidate");

        if (e instanceof UserException) {
            try {
                response.getWriter().write("{\"success\":false,\"message\":\" 请先登录\"}");
            } catch (IOException e1) {
                LOG.error("response error message = {}, e = {}", e1.getMessage(), e);
            }
            return mv;
        }

        if (e instanceof AccessAuthException){
            try {
                response.getWriter().write("{\"success\":false,\"message\":\" 权限不足,请联系管理员\"}");
            } catch (IOException e1) {
                LOG.error("response error message = {}, e = {}", e1.getMessage(), e);
            }
            return mv;
        }

        try {
            LOG.error("ms exceptionHandler error = ", e);
            response.getWriter().write("{\"success\":false,\"message\":\" 服务器开小差\"}");
        } catch (IOException ex) {
            LOG.error("response error message = {}, e = {}", ex.getMessage(), e);
        }

        return mv;
    }
}
