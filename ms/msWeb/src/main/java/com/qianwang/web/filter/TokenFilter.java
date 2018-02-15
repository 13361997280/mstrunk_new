package com.qianwang.web.filter;

import com.qianwang.dao.domain.user.Users;
import com.qianwang.service.user.UserService;
import com.qianwang.util.security.DcPasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenFilter implements Filter{

	/**
	 * 日志记录对象
	 */
	private final static Logger LOG = LoggerFactory.getLogger(TokenFilter.class);

	public static String TOKEN_NAME = "dw-token";
	public static String TOKEN_REFERER_NAME = "dw-token-referer";

	@Autowired
	private ApplicationContext applicationContext;
	private UserService userService;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		Users user =null;
				HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		String uri = request.getRequestURI();
		LOG.info("uri="+uri);

		if (!uri.contains("/content.do") &&
			!uri.contains("/getShortContent.do")
			&& !uri.contains("/MP_verify_ekKexK66BLkoNWuY.txt")
				&& !uri.contains("/monitor.jsp")
				&& !uri.contains("/log/")
				&& !uri.contains("/wechat/token.do")
				&& !uri.contains("/log/search.do")
				&& !uri.contains("/userlabel/profession.do")
				) {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			userService = (UserService) webApplicationContext.getBean("userService");
			String clientReferer = "";
			String userName = "";
			String jSession = "";
			String cookieObj = request.getHeader("Cookie");
			LOG.info("cookieObj1====="+cookieObj);
			if(cookieObj.indexOf("token")==-1)
				cookieObj = request.getHeader("cookie");
			LOG.info("cookieObj2====="+cookieObj);
			if (cookieObj != null && cookieObj.length() > 0) {
				String[] cookies = cookieObj.split(";");
				for (String cookie : cookies) {
					String[] cs = cookie.split("=");
					if (cs.length > 1) {
						if (cs[0].trim().equals("JSESSIONID"))
							jSession = cs[1];
						if (cs[0].trim().equals("userName"))
							userName = cs[1];
						if (cs[0].trim().equals("token"))
							clientReferer = cs[1];
					}
				}
			}
			LOG.info("userName====="+userName+"==clientReferer"+clientReferer+"===jSession"+jSession);
			if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(clientReferer) && StringUtils.isNotEmpty(jSession)) {
				user = userService.loadUser(userName);
				LOG.info("user====="+user);
				if (user==null || !clientReferer.equals(DcPasswordEncoder.encryption(user.getUsername() + "@#@" + user.getPassword()))) {
					response.sendRedirect("/build/index.html#/login");
				}
				LOG.info("token=" + clientReferer + ", user=" + userName);
			} else {
				if (!("/captcha/generate.do".equals(uri) || "/index/userNumber.do".equals(uri) || "/user/loginAction.do".equals(uri) || "/user/registerUser.do".equals(uri))) {
					response.sendRedirect("/build/index.html#/login");
				}
			}
			if (user != null) {
				request.getSession().setAttribute("userId", user.getId());
				request.getSession().setAttribute("userName", user.getUsername());
			}
		/*if(uri.endsWith(".jsp")){
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
		}*/
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
