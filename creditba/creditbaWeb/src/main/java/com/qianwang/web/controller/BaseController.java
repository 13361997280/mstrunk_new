package com.qianwang.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qianwang.credit.entity.BaseBizEntity;
import com.qianwang.credit.util.page.Page;
import com.qianwang.util.Editor.DateEditor;
import com.qianwang.web.result.AjaxResult;
import com.qianwang.web.security.CasUser;
import com.qianwang.web.util.MyDataBinder;
import com.qianwang.web.util.WebUtil;

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

	public String getCurrentUsername() {
		String username = "";
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			LOG.info("BaseController-getCurrentUsername() {} SecurityContext 为空" );
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

	public static long getCurrentUserId() {
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

	protected AjaxResult getSuccessResult() {
		return AjaxResult.successResult(null);
	}
	
	protected AjaxResult getSuccessResult(Object data) {
		return AjaxResult.successResult(data);
	}
	
	protected AjaxResult getFailureResult(String message){
		return AjaxResult.failureResult(message);
	}

	protected HttpServletRequest getRequest(){
		ServletRequestAttributes holder = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = holder.getRequest();
		return request;
	}
	
	protected <T> T getParaBean(Class<T> clazz){
		try {
			T bean = clazz.newInstance();
			this.bindParams(bean);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void bindParams(Object bean){
		Map<?,?> params = this.getParameterMap();
		bindParams(bean, params, null, null);
	}
	
	protected void bindParams(Object bean, String...exclued){
		Map<?,?> params = this.getParameterMap();
		bindParams(bean, params, null, exclued);
	}
	
	@SuppressWarnings("rawtypes")
	protected static BindException bindParams(Object bean , Map map, String[] allowedFields ,String[] disallowedFields){
		DataBinder binder = new MyDataBinder(bean);
		if(allowedFields!=null && allowedFields.length>0){
			binder.setAllowedFields(allowedFields);
		}
		if(disallowedFields!=null && disallowedFields.length>0){
			binder.setDisallowedFields(disallowedFields);
		}
		MutablePropertyValues mpvs = new MutablePropertyValues(map);
		binder.bind(mpvs);
		BindException errors = new BindException(binder.getBindingResult());
		return errors;
	}
	
	protected Integer getIntParam(String name){
		String s = getStrParam(name);
		if(s==null||s.isEmpty()){
			return null;
		}
		return Integer.parseInt(s);
	}
	
	protected Long getLongParam(String name){
		return Long.parseLong(getStrParam(name));
	}
	
	protected String getStrParam(String name){
		Object o = this.getParameterMap().get(name);
		return (o==null ? null : o+"");
	}

    private static final String params_attr_key = "#J7H$%^GIJ>;B9io&Ywer#";
	
	@SuppressWarnings("rawtypes")
	protected Map getParameterMap() {
		HttpServletRequest request = this.getRequest();
		
		Map map = (Map) request.getAttribute(params_attr_key);
		if(map==null){
			if("POST".equals(request.getMethod())){
				map = WebUtil.getBodyParameterMap(request);
			}else{
				map = WebUtil.getParameterMap(request);
			}
			request.setAttribute(params_attr_key, map);
		}
		
		return map;
	}
	
	/**
	 * 分页对象
	 * @param pageSize 默认分页大小,如果用户传值，会覆盖掉
	 * @return
	 */
	protected Page getPage(Integer pageSize){
		Integer pageNo = 1;
		try{
			String pageNo_s = this.getStrParam("pageNo");
			if(pageNo_s!=null){
				pageNo = Integer.parseInt(pageNo_s);
			}
			String pageSize_s = this.getStrParam("pageSize");
			if(pageSize_s!=null){
				pageSize = Integer.parseInt(pageSize_s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Page page = new Page(pageSize, pageNo);
		return page;
	}
	
	@SuppressWarnings("rawtypes")
	protected AjaxResult getPageResult(Page page, List list) {
		AjaxResult ajaxResult = AjaxResult.successResult(list);
		ajaxResult.setRecordsTotal(page.getRowTotal());
		ajaxResult.setTotalCount(page.getPageCount());
		return ajaxResult;
	}


	protected void checkOwner(Object confTask) {
		
	}

	protected void setOwner(BaseBizEntity entity) {
		entity.setOperator(this.getCurrentUsername());
	}

	protected Collection<?> filter(Collection<?> beanList, String...pps){
		for(Object bean:beanList){
			this.filter(bean, pps);	
		}
		return beanList;
	}

	protected Object filter(Object bean, String...pps){
		for(String pp:pps){
			try {
				PropertyUtils.setProperty(bean, pp, null);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return bean;
	}
	
}
