package com.qianwang.web.filter;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.security.CasUser;

/**
 * Created by liuwang on 2016/7/4.
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    private final static Logger LOG = LoggerFactory.getLogger(LogInterceptor.class);

    private final static ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();

        if(url!=null && url.contains(".do")){
            long userId = BaseController.getCurrentUserId();
            if(0 == userId){
                PrintWriter writer = response.getWriter();
                writer.write("{\"success\":false,\"message\":\"please order first"+"\",\"returnCode\":"+ ExceptionEnum.ASS_GO_LOGIN_EXCEPTION.key+"}");
                writer.flush();
                writer.close();
                return false;
            }
           /* if(url.contains("api/v34/account4Client/login.do")

                    )  {
            }else{
                String t = request.getSession().getAttribute("HAS_NOT_ORDER")==null?"":request.getSession().getAttribute("HAS_NOT_ORDER").toString();
//                LOG.info("LogInterceptor-preHandle():{}:HAS_NOT_ORDER:{}", t);
                if(t.equals("true")){
                    SecurityContext context = SecurityContextHolder.getContext();
                    LOG.info("LogInterceptor-preHandle():context:{}", context);
                    PrintWriter writer = response.getWriter();
                    if (context != null){
                        Authentication authentication = context.getAuthentication();
                        if (authentication != null) {
                            Object principal = authentication.getPrincipal();
                            if (principal instanceof CasUser) {
                                CasUser cu=(CasUser) principal;
                                writer.write("{\"success\":false,\"message\":\"please order first"+"\",\"returnCode\":"+ ExceptionEnum.ASS_NOT_SUBSCRIBED_EXCEPTION.key+",\"data\":{\"payMoney\":"+request.getSession().getAttribute("PAY_MONEY").toString()+",\"userId\":\""+cu.getUserId()+"\",\"userName\":\""+cu.getUserName()+"\",\"nikeName\":\""+cu.getNickName()+"\"}}");
                            }else{
                                writer.write("{\"success\":false,\"message\":\"please order first"+"\",\"returnCode\":"+ ExceptionEnum.ASS_NOT_SUBSCRIBED_EXCEPTION.key+",\"data\":{\"payMoney\":"+request.getSession().getAttribute("PAY_MONEY").toString()+"}}");
                            }
                        }
                    }else {
                        writer.write("{\"success\":false,\"message\":\"please order first"+"\",\"returnCode\":"+ ExceptionEnum.ASS_NOT_SUBSCRIBED_EXCEPTION.key+",\"data\":{\"payMoney\":"+request.getSession().getAttribute("PAY_MONEY").toString()+",\"userId\":\"-1\",\"userName\":\"宝粉\",\"nikeName\":\"宝粉\"}}");
                    }
                    writer.flush();
                    writer.close();
                    return false;
                }else if(StringUtils.isEmpty(t)){
                    long userId=baseController.getCurrentUserId();
                    if(0==userId){
                        PrintWriter writer = response.getWriter();
                        writer.write("{\"success\":false,\"message\":\"please order first"+"\",\"returnCode\":"+ ExceptionEnum.ASS_GO_LOGIN_EXCEPTION.key+"}");
                        writer.flush();
                        writer.close();
                        return false;
                    }

                }
            }*/
        }

        long startTime = System.currentTimeMillis();
        request.setAttribute("Interceptor_StartTime", startTime);
        String clazzName = ((HandlerMethod) handler).getMethod().getDeclaringClass().getName();
        String indexInfo = clazzName.substring(clazzName.lastIndexOf(".") + 1) + "." + ((HandlerMethod) handler).getMethod().getName();
        //HttpSession session = request.getSession();

        StringBuffer sb = new StringBuffer();
        Enumeration<String> a = null;
        a = request.getParameterNames();
        while (a.hasMoreElements()) {
            String key = a.nextElement();
            sb.append(key + ":" + request.getParameter(key) + ", ");
        }
//        LOG.info("[{}]- IP: {}, URL: {}, Method: {}, Params:[{}]", indexInfo, ip,
//                url,request.getMethod(), StringUtils.removeEnd(StringUtils.trim(sb.toString()), ","));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("Interceptor_StartTime");
        request.removeAttribute("Interceptor_StartTime");
        long endTime = System.currentTimeMillis();
        HandlerMethod handler2 = (HandlerMethod) handler;
        String clazzName = handler2.getMethod().getDeclaringClass().getName();
        String indexInfo = clazzName.substring(clazzName.lastIndexOf(".") + 1) + "." + handler2.getMethod().getName();
        if(modelAndView != null){
            SecurityContext context = SecurityContextHolder.getContext();
            LOG.info("LogInterceptor-postHandle():context:{}", context);
            if (context != null){
                Authentication authentication = context.getAuthentication();
                if (authentication != null) {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof CasUser) {
                        CasUser cu=(CasUser) principal;
                        LOG.info("[{}]- userId: {}, userName: {}, mobile: {}, ResultSet: {}, ViewName:{}", indexInfo,cu.getUserId(),cu.getUserName(),cu.getMobile(),jsonMapper.writeValueAsString(modelAndView.getModel()),modelAndView.getViewName());
                    }
                }
            }else {
                LOG.info("[{}]- userId: {}, userName: {}, mobile: {}, ResultSet: {}, ViewName:{}", indexInfo,0,"","",jsonMapper.writeValueAsString(modelAndView.getModel()),modelAndView.getViewName());
            }
        }
//        LOG.info("[{}]- Time: {}(ms)",indexInfo, endTime - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}
