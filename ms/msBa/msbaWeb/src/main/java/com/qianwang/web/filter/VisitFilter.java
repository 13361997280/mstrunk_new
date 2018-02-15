package com.qianwang.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.qianwang.dao.util.SpringContextHolder;
import com.qianwang.service.authority.AuthorityService;

public class VisitFilter implements Filter{

	/**
	 * 日志记录对象
	 */
	protected static final Log LOG = LogFactory.getLog(VisitFilter.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		String username = "";
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		Authentication authentication = context.getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			}
		}
		String url = request.getRequestURL().toString();
		LOG.info("系统管理员:"+username+"正在访问"+url);
		if(StringUtils.isEmpty(username)){
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		if(!hasAuthority(request)){
			response.sendRedirect("/common/403.jsp");
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	private boolean hasAuthority(HttpServletRequest request){
		AuthorityService authorityService = (AuthorityService)SpringContextHolder.getApplicationContext().getBean("authorityService");

		String url = request.getRequestURI();
		int end = url.indexOf("?");
		if(end > 0){
			url = url.substring(0,end);
		}
		url = url.replaceFirst("/", "");
		
		if(url.equals("index.jsp")) return true;
		
		/*if(!authorityService.hasMenus(url)) return true;
		
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}
		Authentication authentication = context.getAuthentication();
		boolean hasAuthorty = false;
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails ud = (UserDetails) principal;
				for (Object obj : ud.getAuthorities()) {
					hasAuthorty = authorityService.hasMenuAuthorities(url, obj.toString());
					if(hasAuthorty) break;
				}
			}
		}
		return hasAuthorty;*/
		return true;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
