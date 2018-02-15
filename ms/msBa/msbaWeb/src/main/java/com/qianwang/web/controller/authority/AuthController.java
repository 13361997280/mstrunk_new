package com.qianwang.web.controller.authority;

import com.qianwang.dao.domain.user.UserView;
import com.qianwang.service.user.UserService;
import com.qianwang.util.security.DcPasswordEncoder;
import com.qianwang.web.config.SecondaryCheckList;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.filter.SecondaryCheckFilter;
import com.qianwang.web.result.AjaxResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/auth/**")
public class AuthController extends BaseController{
	private static final Log log = LogFactory.getLog(AuthController.class);
	@Autowired
	private UserService userService;
/*	@Autowired
	SendSMS sendSMS;*/
	@Autowired
	SecondaryCheckList secondaryCheckList;
	
	@Autowired
    @Qualifier("org.springframework.security.authenticationManager")
    protected AuthenticationManager authenticationManager;

	/**
	 * 查询用户信息列表
	 * 
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/findUsers", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult findUsers() {
		return AjaxResult.successResult(userService.findAllUser());
	}

	/**
	 * 保存用户信息
	 * 
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult insertUser(UserView user) {
		user.setPassword(new DcPasswordEncoder().encodePassword(user.getPassword(), user.getUsername()));
		user.setCreateTime(new Date());
		user.setCreateUsername(getCurrentUsername());
		userService.insertUser(user);
		return AjaxResult.successResult(user.getUsername());
	}

	/**
	 * 更新用户信息
	 * 
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult updateUser(UserView user) {
		if(StringUtils.isNotEmpty(user.getPassword())){
			user.setPassword(new DcPasswordEncoder().encodePassword(user.getPassword(), user.getUsername()));
		}
		userService.updateUser(user);
		return AjaxResult.successResult(user.getUsername());
	}

	/**
	 * 根据用户名加载用户
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/loadUser")
	@ResponseBody
	public AjaxResult loadUser(String username) {
		UserView user = userService.loadUser(username);
		return new AjaxResult(user != null, user);
	}
	
	/**
	 * 根据用户名加载用户
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/phone")
	public ModelAndView phone(HttpServletRequest request,String username,String password) {
	    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	    		username, password);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = null;
        try{
        	authenticatedUser = authenticationManager.authenticate(token);
        }catch(AuthenticationException e){
        	return new ModelAndView("/common/403.jsp");
        }
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        if(hasAuthority("ROLE_ADMIN") || hasAuthority("ROLE_ACCESS_ADV")){
        	return new ModelAndView("/pages/phone/index.jsp");
        }
        return new ModelAndView("/common/403.jsp");
	}

	/**
	 * 更新用户信息
	 * 
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/deleteUser")
	@ResponseBody
	public AjaxResult deleteUser(String username) {
		userService.deleteUser(username);
		return AjaxResult.successResult(username);
	}

	/**
	 * 配置用户权限信息
	 * @param username 用户名
	 * @param authorities 逗号分隔权限集合
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/configUser")
	@ResponseBody
	public AjaxResult configUser(String username,String authorities) {
		userService.configUser(username, authorities);
		return AjaxResult.successResult(username);
	}
	
	/**
	 * 根据用户名查询权限菜单
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/findUserMenu")
	@ResponseBody
	public AjaxResult findUserMenu(int parentId){
		return AjaxResult.successResult(userService.findUserMenu(getCurrentUsername(), parentId));
	}

	@RequestMapping(value = "/findUserInfoByMobile")
	@ResponseBody
	public AjaxResult findUserInfoByMobile(String mobile){
		return AjaxResult.successResult(userService.findUserInfoByMobile(mobile));
	}


	/**
	 * 更新用户信息
	 *
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult modifyPwd(String oldPwd,String newPwd) {
		String username = getCurrentUsername();
		UserView user = userService.loadUser(username);
		String op = user.getPassword();
		String pwd = new DcPasswordEncoder().encodePassword(oldPwd, user.getUsername());
		if(op.equals(pwd)){
			user.setPassword(new DcPasswordEncoder().encodePassword(newPwd, user.getUsername()));
			userService.updateUser(user);
			return AjaxResult.successResult(user.getUsername());
		}else{
			return AjaxResult.failureResult("修改失败，原密码错误");
		}
	}

	/**
	 * 更新用户信息
	 *
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/hasModifyPass")
	@ResponseBody
	public AjaxResult hasModifyPass() {
		return AjaxResult.successResult(userService.hasModifyPass(getCurrentUsername()));
	}

	/**
	 * 更新用户信息
	 *
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/hasBindMobile")
	@ResponseBody
	public AjaxResult hasBindMobile() {
		return AjaxResult.successResult(userService.hasBindMobile(getCurrentUsername()));
	}


	/**
	 * 更新用户信息
	 *
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/hasToBindMobile")
	@ResponseBody
	public AjaxResult hasToBindMobile() {
		String username = getCurrentUsername();
		boolean hasToBindMobile = userService.hasBindMobile(username);
		if(!hasToBindMobile){
			return AjaxResult.successResult(false);
		}
		for(String whiteUser : secondaryCheckList.getWhiteList()){
			if(username.equals(whiteUser)){
				return AjaxResult.successResult(false);
			}
		}
		return AjaxResult.successResult(true);
	}

	/**
	 * 更新用户信息
	 *
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult bindMobile(String mobile,String smsCode, HttpServletRequest request) {
		String username = getCurrentUsername();
		UserView user = userService.loadUser(username);
		String smsCodeServer = (String)request.getSession().getAttribute("smsCode");
		if(smsCodeServer.equalsIgnoreCase(smsCode)){
			user.setMobile(mobile);
			userService.bindMobile(user);
			return AjaxResult.successResult(user.getUsername());
		}else{
			return AjaxResult.failureResult("修改失败，原密码错误");
		}
	}

	/**
	 * 更新用户信息
	 *
	 * @return 返回用户json数据
	 */
	@RequestMapping(value = "/sendBindMobileSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendBindMobileSmsCode(String mobile, HttpServletRequest request) {
		String username = getCurrentUsername();
		UserView user = userService.loadUser(username);
		Object lastSendCodeTime = request.getSession().getAttribute("smsCodeTime");
		Long currTime = System.currentTimeMillis();
		request.getSession().setAttribute("smsCodeTime", currTime);
		if(lastSendCodeTime != null && (currTime-(Long)lastSendCodeTime < 50000)){
			return AjaxResult.failureResult("您请求验证码过于频繁，请稍后再试!");
		}
		if(user.isBindMobile()){
			String smsCode = geneSmsCode();
			request.getSession().setAttribute("smsCode", smsCode);
			try {
				String msg = "欢迎使用数据墙，您此次操作的验证码是：" + smsCode;
				log.info(msg);
				/*sendSMS.sendSMS(48, mobile, msg, smsCode, 2);*/
			} catch (Exception e) {
				e.printStackTrace();
				return AjaxResult.failureResult("请求出错，请稍后再试!");
			}
			return AjaxResult.successResult("验证码已发送，请注意查收!");
		}else{
			return AjaxResult.failureResult("登陆用户已绑定手机号！");
		}
	}

	@RequestMapping(value = "/sendCheckMobileSmsCode", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult sendCheckMobileSmsCode(HttpServletRequest request) {
		String username = getCurrentUsername();
		UserView user = userService.loadUser(username);
		Object lastSendCodeTime = request.getSession().getAttribute("smsCheckCodeTime");
		Long currTime = System.currentTimeMillis();
		request.getSession().setAttribute("smsCheckCodeTime", currTime);
		if(lastSendCodeTime != null && (currTime-(Long)lastSendCodeTime < 50000)){
			return AjaxResult.failureResult("您请求验证码过于频繁，请稍后再试!");
		}
		if(!user.isBindMobile() && StringUtils.isNotBlank(user.getMobile())){
			String smsCode = geneSmsCode();
			request.getSession().setAttribute("smsCheckCode", smsCode);
			try {
				String msg = "欢迎使用数据墙，您此次操作的验证码是：" + smsCode;
				log.info(msg);
/*
				sendSMS.sendSMS(48, user.getMobile(), msg, smsCode, 2);
*/
			} catch (Exception e) {
				e.printStackTrace();
				return AjaxResult.failureResult("请求出错，请稍后再试!");
			}
			return AjaxResult.successResult("验证码已发送，请注意查收!");
		}else{
			return AjaxResult.failureResult("请先绑定手机号！");
		}
	}

	@RequestMapping(value = "/secondCheckCode")
	@ResponseBody
	public AjaxResult secondCheckCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String type = request.getParameter("type");
		String val = request.getParameter("val");
		boolean flag = false;
		String msg = "验证码错误！";
		if("1".equals(type)){
			String smsCheckCodeServer = (String)request.getSession().getAttribute("smsCheckCode");
			if(smsCheckCodeServer.equalsIgnoreCase(val)) {
				flag = true;
			}
		}else if("2".equals(type)){
			String username = getCurrentUsername();
			UserView user = userService.loadUser(username);
			List<Map<String, String>> userAccountInfo = userService.loadUserQbAccountInfo(user.getMobile());
			if(userAccountInfo != null && userAccountInfo.size() > 0){
				String accountName = userAccountInfo.get(0).get("username");
				String subName = accountName.substring(accountName.length()-6,accountName.length());
				if(subName.equals(val)){
					flag = true;
				}
			}else{
				msg = "您绑定的手机非钱宝账户手机，请使用其它验证方式！";
			}
		}
		if(flag){
			request.getSession().setAttribute(SecondaryCheckFilter.SECONDARY_CHECK_FLAG, "true");
			return AjaxResult.successResult("验证成功！");
		} else {
			return AjaxResult.failureResult(msg);
		}
	}

	@RequestMapping(value = "/getUserInfoByMobileInfo", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult getUserPhoneInfo(String mobile) {
		String username = getCurrentUsername();
		UserView user = userService.loadUser(username);
		return AjaxResult.successResult(user.getMobile().substring(0,3)+"******"+user.getMobile().substring(9,11));
	}

	private static String[] beforeShuffle = new String[] {"0","1","2","3","4","5","6","7",
			"8","9","A","B","C","D","E","F","G","H","I","J",
			"K","L","M","N","O","P","Q","R","S","T","U","V",
			"W","X","Y","Z"};
	private String geneSmsCode(){
		List list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(5, 11);
		return result;
	}
}
