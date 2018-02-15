package com.qianwang.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianwang.util.Editor.DateEditor;
import com.qianwang.util.lang.DateUtil;

/**
 * 控制器基类
 * 
 * @author zou
 * 
 */
public class BaseController {
	
	/**
	 * 日志记录对象
	 */
	protected static final Log LOG = LogFactory.getLog(BaseController.class);
	
	/**
	 * 返回当前登录用户名
	 * @return
	 */
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
	}
	

	/**
	 * 返回当前登录用户名
	 * @return
	 */
	public boolean hasAuthority(String authority) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}
		Authentication authentication = context.getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails ud = (UserDetails) principal;
				for (Object obj : ud.getAuthorities()) {
					if(authority.equals(obj.toString())){
						return true;
					} 
				}
			}
		}
		return false;
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
	
	protected Date getDateParams(HttpServletRequest request,String name){
		try {
			return DateUtil.parseDate(request.getParameter(name), DateUtil.SHORT_TIMESTAMP_PATTERN);
		} catch (ParseException e) {
			LOG.error("error in exportFinanceContrast:" + e);
		}
		return null;
	}
	
	protected Integer getIntegerParams(HttpServletRequest request,String name){
		String dataStr = request.getParameter(name);
		if(!StringUtils.isEmpty(dataStr) && !"null".equals(dataStr)){
			return Integer.parseInt(dataStr);
		}
		return null;
	}
	
	protected String getSort(HttpServletRequest request){
		String oc = request.getParameter("order[0][column]");
		String sc = request.getParameter("order[0][dir]");
		String field = "";
		@SuppressWarnings("unchecked")
		Map<String,String[]> params = request.getParameterMap();
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			if(param.getKey().indexOf(oc) >=0 && param.getKey().indexOf("data") >= 0){
				field = param.getValue()[0];
				break;
			}
		}
		return field + " " + sc;	
	}
	
	@RequestMapping(value = "/exportExcel")
	protected void exportExcel(HttpServletRequest request, HttpServletResponse response,
			String columnText, String columnIndex,String exportList,String title,String fileName){
		LOG.info("BaseController-exportExcel||"+getCurrentUsername()+"||"
				+ request.getRequestURI()
				+ (request.getParameterMap().size() > 0 ? ("?"+request.getQueryString()):""));
		response.setContentType("octets/stream");
		response.setCharacterEncoding("utf-8");
		try {
			fileName = URLDecoder.decode(fileName, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + 
					URLEncoder.encode(fileName,"utf-8").replace("+","%20") + ".xls");
			columnText = URLDecoder.decode(columnText, "UTF-8");
			title = URLDecoder.decode(title, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("error in exportExcel : " + e);
		}
		String headers[] = columnText.split(",");
		columnIndex = columnIndex.replace("pointsPlatformId", "pointsPlatform");
		String fields[] = columnIndex.split(",");
		List<?> l = new ArrayList<Object>();
		try {
			Method method = this.getClass().getDeclaredMethod(exportList,HttpServletRequest.class);
			l = (List<?>)method.invoke(this,request);//
		} catch (SecurityException e) {
			LOG.error("error in exportExcel : " + e);
		} catch (NoSuchMethodException e) {
			LOG.error("error in exportExcel : " + e);
		} catch (IllegalArgumentException e) {
			LOG.error("error in exportExcel : " + e);
		} catch (IllegalAccessException e) {
			LOG.error("error in exportExcel : " + e);
		} catch (InvocationTargetException e) {
			LOG.error("error in exportExcel : " + e);
		}
		
		/*ExportExcel ex = new ExportExcel();
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			ex.exportExcel(title, headers, fields, l, out, "yyyy-MM-dd");
		} catch (IOException e) {
			LOG.error("error in exportExcel : " + e);
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				LOG.error("error in exportExcel : " + e);
			}
		}*/
	}
	
	
}
