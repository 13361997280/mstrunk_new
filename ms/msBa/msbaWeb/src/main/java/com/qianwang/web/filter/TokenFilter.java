package com.qianwang.web.filter;

import com.qianwang.dao.util.SpringContextHolder;
import com.qianwang.service.authority.AuthorityService;
import com.qianwang.web.util.TokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

public class TokenFilter implements Filter{

	/**
	 * 日志记录对象
	 */
	protected static final Log LOG = LogFactory.getLog(TokenFilter.class);

	public static String TOKEN_NAME = "dw-token";
	public static String TOKEN_REFERER_NAME = "dw-token-referer";

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
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		String uri = request.getRequestURI();
		if(uri.endsWith(".jsp")){
			request.getSession().setAttribute(TOKEN_NAME, TokenGenerator.getInstance().generateTokeCode());
			request.getSession().setAttribute(TOKEN_REFERER_NAME, request.getRequestURL().toString());
		} else if(uri.endsWith(".do") && !"/captcha/generate.do".equals(uri)
				&& !uri.matches("^/auth/.+\\.do$")
				&& !uri.matches("^/params/.+\\.do$")){
			String clientToken = request.getParameter(TOKEN_NAME);
			String clientReferer = request.getHeader("Referer");
			String token = (String)request.getSession().getAttribute(TOKEN_NAME);
			String tokenReferer = (String)request.getSession().getAttribute(TOKEN_REFERER_NAME);
			if(StringUtils.isBlank(token) || StringUtils.isBlank(clientToken) || StringUtils.isBlank(clientReferer)
					|| !clientToken.equals(token) || !clientReferer.equals(tokenReferer)){
				response.sendRedirect("/common/403.jsp?from="+uri);
			}
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * 判断ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjax(HttpServletRequest request){
		return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())   ) ;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
