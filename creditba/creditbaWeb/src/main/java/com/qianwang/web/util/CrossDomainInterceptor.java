package com.qianwang.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 设置跨域头
 * @author wangjg
 * 
 */
public class CrossDomainInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(CrossDomainInterceptor.class);
	private String domain = "*";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(logger.isDebugEnabled()){
		}
		response.setHeader("Access-Control-Allow-Origin", domain);
		if("OPTIONS".equals(request.getMethod())){
			response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
			response.setHeader("Access-Control-Allow-Headers", "x-requested-with, accept, origin, content-type");
			response.setStatus(200);
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
