package com.qianwang.web.filter;

import com.qianwang.dao.util.SpringContextHolder;
import com.qianwang.service.authority.AuthorityService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VisitFilter implements Filter {

    /**
     * 日志记录对象
     */
    protected static final Logger LOG = LoggerFactory.getLogger(VisitFilter.class);

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
        LOG.info(username + "|" + url);
        if (StringUtils.isEmpty(username)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!hasAuthority(request)) {
            response.sendRedirect("/common/403.jsp");
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    private boolean hasAuthority(HttpServletRequest request) {
        AuthorityService authorityService = (AuthorityService) SpringContextHolder.getApplicationContext().getBean("authorityService");

        String url = request.getRequestURI();
        int end = url.indexOf("?");
        if (end > 0) {
            url = url.substring(0, end);
        }
        url = url.replaceFirst("/", "");

        if (url.equals("index.jsp")) return true;

//		if(!authorityService.hasMenus(url)) return true;

//		SecurityContext context = SecurityContextHolder.getContext();
//		if (context == null) {
//			return false;
//		}
//		Authentication authentication = context.getAuthentication();
//		boolean hasAuthorty = false;
//		if (authentication != null) {
//			Object principal = authentication.getPrincipal();
//			if (principal instanceof UserDetails) {
//				UserDetails ud = (UserDetails) principal;
//				for (Object obj : ud.getAuthorities()) {
//					hasAuthorty = authorityService.hasMenuAuthorities(url, obj.toString());
//					if(hasAuthorty) break;
//				}
//			}
//		}
//		return hasAuthorty;
        return true;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
