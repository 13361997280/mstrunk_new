package com.qianwang.web.controller;

import com.qianwang.util.Editor.DateEditor;
import com.qianwang.web.security.CasUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 控制器基类
 * 
 * @author zou
 * 
 */
@Component
public class BaseController {
	
	/**
	 * 日志记录对象
	 */
	protected static final Log LOG = LogFactory.getLog(BaseController.class);

	public String getCurrentUsername() {
		String username = "";
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return username;
		}
		Authentication authentication = context.getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			}
		}
		return username;
//        return "syy002";
	}

	public long getCurrentUserId() {
		long userId = 0L;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return userId;
		}
		Authentication authentication = context.getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof CasUser) {
				userId = ((CasUser) principal).getUserId();
			}
		}
//        if (userId == 0) {
//            if (isAjax(request)) {
//                throw new AjaxNotLoginException();
//            } else {
//                throw new NotLoginException();
//            }
//        }
		return userId;
//       return 152259;
	}

	/**
	 * 处理日期行参数
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	protected void initBinder(HttpServletRequest request,
							  ServletRequestDataBinder binder) throws Exception {
		//对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
	}


}
