package com.qianwang.web.controller.user;

import com.qianwang.dao.domain.user.Users;
import com.qianwang.service.user.UserService;
import com.qianwang.util.security.CaptchaUtil;
import com.qianwang.util.security.DcPasswordEncoder;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.SimpleResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/loginAction")
	@ResponseBody
	public SimpleResult loginAction(HttpServletRequest request, HttpServletResponse response,@RequestBody Map requestParam)throws Exception{
		try {
			String userName =requestParam.get("j_username").toString();
			String password =requestParam.get("j_password").toString();
			if(userName != null && userName.contains(" ")){
				return SimpleResult.failureResult("用户名不能包含空格！",null);
			}
			if(password != null && password.contains(" ")){
				return SimpleResult.failureResult("密码不能包含空格！",null);
			}
			if(StringUtils.isEmpty(userName)){
				return SimpleResult.failureResult("用户名不能为空！",null);
			}
			if(StringUtils.isEmpty(password)){
				return SimpleResult.failureResult("密码不能为空！",null);
			}
			if(!checkValidateCode(request,requestParam)){
				return SimpleResult.failureResult("验证码错误，请输入正确的验证码",null);
			}
			Users user = userService.loadUser(userName);

			if(user == null || user.getUsername()== null){
				return SimpleResult.failureResult("用户名不存在，请注册！",null);
			}

			if (!password .equals(user.getPassword())){
				return SimpleResult.failureResult("密码错误！请输入正确的登录密码",null);
			}

			Map tokenMap = new HashMap();
			tokenMap.put("token", DcPasswordEncoder.encryption(user.getUsername() + "@#@" + user.getPassword()));
			tokenMap.put("userName", user.getUsername());
			tokenMap.put("userId", user.getId());
			return SimpleResult.successResult("登录成功!", tokenMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SimpleResult.failureResult("登录失败！",null);
	}

	@RequestMapping(value = "/registerUser")
	@ResponseBody
	public SimpleResult registerUser(HttpServletRequest request, HttpServletResponse response,@RequestBody Map requestParam)throws Exception{
		Integer userId =null;
		try {
			String userName =requestParam.get("username").toString();
			if(userName!=null && userName.contains(" ")){
				return SimpleResult.failureResult("用户名不能包含空格！",null);
			}
			if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(userName.trim())){
                return SimpleResult.failureResult("注册用户名不能为空！",null);
            }
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(userName.trim());
			if(!matcher.matches()){
				return SimpleResult.failureResult("用户名必须是邮箱地址！",null);
			}
			Users user=userService.loadUser(userName);
			if(user!=null && user.getUsername().length()>0){
				return SimpleResult.failureResult("用户名已存在！",null);
			}
			String password =requestParam.get("password").toString();
			if(StringUtils.isEmpty(password)){
                return SimpleResult.failureResult("密码不能为空！",null);
            }
			userId = userService.insertUser(new Users(userName,password,true,"","1","",new Date(),new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId!=null ? SimpleResult.successResult("注册成功!",null):SimpleResult.failureResult("注册失败!",null);
	}


/*	@RequestMapping(value = "/userNumber")
	@ResponseBody
	public SimpleResult userNumber(HttpServletRequest request, HttpServletResponse response)throws Exception{

		return SimpleResult.successResult("用户数量","888888");
	}*/

	/**
	 * 验证用户输入的验证是否正确
	 * @param request
	 */
	protected boolean checkValidateCode(HttpServletRequest request,Map requestParam) {
		boolean flag=true;
		String sessionValidateCode = (String) request.getSession().getAttribute(CaptchaUtil.VALID_CODE_MARK);
		if(StringUtils.isEmpty(sessionValidateCode)||requestParam.get("validCode")==null){
			flag=false;
			//return SimpleResult.failureResult("验证码错误，请输入正确的验证码",null);
		}
		//String validateCodeParameter =  request.getParameter(CaptchaUtil.VALID_CODE_MARK);
		String validateCodeParameter =requestParam.get("validCode").toString();
		if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			request.getSession().removeAttribute(CaptchaUtil.VALID_CODE_MARK);
			requestParam.remove("validCode");
			flag=false;
			//return SimpleResult.failureResult("验证码错误，请输入正确的验证码",null);
		}
		return flag;
	}
}
