package com.qianwang.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.qianwang.util.security.CaptchaUtil;

/**
 * 用户登录验证码过滤
 * 
 * @author zou
 * 
 */
public class VcupAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	/**
	 * 是否只支持post提交验证
	 */
	private boolean postOnly;

	/**
	 * 验证用户登录信息
	 */
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST"))
			throw new AuthenticationServiceException((new StringBuilder()).append(
					"Authentication method not supported: ").append(request.getMethod()).toString());
		checkValidateCode(request);
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if (username == null)
			username = "";
		if (password == null)
			password = "";
		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		setDetails(request, authRequest);
		return getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 验证用户输入的验证是否正确
	 * @param request
	 */
	protected void checkValidateCode(HttpServletRequest request) {
/*		String sessionValidateCode = obtainSessionValidateCode(request);
		if(StringUtils.isEmpty(sessionValidateCode)){
			throw new AuthenticationServiceException(messages.getMessage("validateCode.notEquals"));
		}
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			request.getSession().removeAttribute(CaptchaUtil.VALID_CODE_MARK);
			throw new AuthenticationServiceException(messages.getMessage("validateCode.notEquals"));
		}*/
	}

	/**
	 * 返回session中保存的验证码
	 * @param request
	 * @return session中保存的验证码
	 */
	protected String obtainSessionValidateCode(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(CaptchaUtil.VALID_CODE_MARK);
	}

	/**
	 * 返回用户输入的验证码
	 * @param request
	 * @return 用户输入的验证码
	 */
	protected String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(CaptchaUtil.VALID_CODE_MARK);
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

}
