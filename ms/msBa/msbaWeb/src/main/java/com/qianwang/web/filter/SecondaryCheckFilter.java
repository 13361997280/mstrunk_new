package com.qianwang.web.filter;

import com.qianwang.web.config.SecondaryCheckList;
import com.qianwang.web.util.TokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecondaryCheckFilter implements Filter {

	/**
	 * 日志记录对象
	 */
	protected static final Log LOG = LogFactory.getLog(SecondaryCheckFilter.class);

	public static String SECONDARY_CHECK_FLAG = "secondary-check-flag";

	private SecondaryCheckList secondaryCheckList;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		ServletContext context = filterConfig.getServletContext();
//		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
//		secondaryCheckList = (SecondaryCheckList) ctx.getBean("secondaryCheckList");
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

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		secondaryCheckList = (SecondaryCheckList)webApplicationContext.getBean("secondaryCheckList");

		Authentication authentication = context.getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
				for(String whiteUser : secondaryCheckList.getWhiteList()){
					if(username.equals(whiteUser)){
						chain.doFilter(servletRequest, servletResponse);
						return;
					}
				}
			}
		}

		String uri = request.getRequestURI();
		String flag = (String)request.getSession().getAttribute(SECONDARY_CHECK_FLAG);
		if(!StringUtils.isBlank(flag)){
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		boolean needCheck = false;
		for (String theUri : secondaryCheckList.getUris()) {
			if (uri.matches(theUri)) {
				needCheck = true;
				break;
			}
		}
		if (needCheck) {
			response.sendRedirect("/secondaryCheck.jsp?redirectUrl=" + uri);
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
