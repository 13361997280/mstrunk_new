package com.qianwang.web.controller.authority;

import com.qianwang.dao.domain.user.Users;
import com.qianwang.service.user.UserService;
import com.qianwang.util.security.DcPasswordEncoder;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;


    /**
     * 保存用户信息
     *
     * @return 返回用户json数据
     */
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertUser(Users user) {
        user.setPassword(new DcPasswordEncoder().encodePassword(user.getPassword(), user.getUsername()));
        user.setCreateTime(new Date());
        userService.insertUser(user);
        return AjaxResult.successResult(user.getUsername());
    }


    /**
     * 保存用户信息
     *
     * @return 返回用户json数据
     *//*
    @RequestMapping(value = "/testCookiesStatus")
	@ResponseBody
	public ModelAndView testCookies(HttpServletRequest request,HttpServletResponse response) {
		//String username=getCurrentUsername();
		ModelAndView mv = new ModelAndView();
		*//*if(username == null || username.length() ==0){
            mv.setViewName("redirect:/login.jsp");
			return mv;
		}*//*
		Cookie cookie = new Cookie("u","admin");
		cookie.setMaxAge(3600 * 12);
		cookie.setPath("/");
		response.addCookie(cookie);
		mv.setViewName("redirect:/index.jsp");
		return mv;
	}*/

    /**
     * 更新用户信息
     *
     * @return 返回用户json数据
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUser(Users user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            user.setPassword(new DcPasswordEncoder().encodePassword(user.getPassword(), user.getUsername()));
        }
        userService.updateUser(user);
        return AjaxResult.successResult(user.getUsername());
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
     *
     * @param username    用户名
     * @param authorities 逗号分隔权限集合
     * @return 返回用户json数据
     */
    @RequestMapping(value = "/configUser")
    @ResponseBody
    public AjaxResult configUser(String username, String authorities) {
        userService.configUser(username, authorities);
        return AjaxResult.successResult(username);
    }


    @RequestMapping(value = "/findUserInfoByMobile")
    @ResponseBody
    public AjaxResult findUserInfoByMobile(String mobile) {
        return AjaxResult.successResult(userService.findUserInfoByMobile(mobile));
    }


    private static String[] beforeShuffle = new String[]{"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private String geneSmsCode() {
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
