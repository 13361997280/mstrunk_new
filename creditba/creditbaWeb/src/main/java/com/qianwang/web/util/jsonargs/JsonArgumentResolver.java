package com.qianwang.web.util.jsonargs;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.qianwang.util.json.JsonUtil;

/**
 * 
 * @author wangjg
 *
 */
public class JsonArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(JsonArgumentResolver.class);
    
    private static final String params_attr_key = "#J7H$%^GIJ>;B9io&Ywer#";
    
	@Override
    public boolean supportsParameter(MethodParameter parameter) {
        JsonArgs o1 = parameter.getParameterAnnotation(JsonArgs.class);
		JsonArgs o2 = parameter.getMethodAnnotation(JsonArgs.class);
		boolean o3 = parameter.getDeclaringClass().isAnnotationPresent(JsonArgs.class);
		String pn = parameter.getParameterName();
		
		if(parameter.getMethodAnnotation(IgnoreJsonArgs.class)!=null){
			return false;
		}
		if(parameter.getMethodAnnotation(JsonArgs.class)!=null){
			return true;
		}
		
		Class<?> clazz = parameter.getDeclaringClass();
		if(clazz.isAnnotationPresent(IgnoreJsonArgs.class)){
			return false;
		}
		if(clazz.isAnnotationPresent(JsonArgs.class)){
			return true;
		}
		if(clazz.getSuperclass().isAnnotationPresent(JsonArgs.class)){
			return true;
		}
		
		return false;
    }

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String paramName = parameter.getParameterName();
		Class<?> paramType = parameter.getParameterType();
		Object paramValue = null;
		if("POST".equals(request.getMethod())){
			paramValue = this.getParamValue(request, paramName);
		}else{
			paramValue = request.getParameter(paramName);
		}
		if(paramValue==null){
			return null;
		}
		paramValue = ConvertUtils.convert(paramValue, paramType);
		return paramValue;
	}
	
	private Object getParamValue(HttpServletRequest request, String name){
		Map<String,String> map = this.getParamMap(request);
		return map.get(name);
	}
	
	private Map<String,String> getParamMap(HttpServletRequest request){
		Map map = (Map) request.getAttribute(params_attr_key);
		if(map==null){
			String requestBody = getRequestBody(request);
			if(StringUtils.isNotBlank(requestBody)){
				map = JsonUtil.jsonToMap(requestBody);
			}else{
				map = Collections.emptyMap();
			}
			request.setAttribute(params_attr_key, map);
		}
		return map;
	}
	
	private static String getRequestBody(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int len = -1;
			while((len=reader.read(buf))!=-1){
				sb.append(buf, 0, len);
			}
			return sb.toString();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	static {
		 ConvertUtils.register(new MyDateConverter(), java.util.Date.class);
	}

}

/*
	<mvc:annotation-driven>
	    <mvc:argument-resolvers>
			<bean class="com.qbao.wyhw.test.JsonArgumentResolver"/>
	    </mvc:argument-resolvers>
		<mvc:message-converters register-defaults="true">
            <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
                <property name="supportedMediaTypes">
                    <list>
                        <value>text/html;charset=UTF-8</value>
                        <value>application/json</value>
                    </list>
                </property>
                <property name="features">
                    <list>
                        <value>QuoteFieldNames</value>
                        <value>WriteDateUseDateFormat</value>
                    </list>
                </property>
            </bean>
        </mvc:message-converters>
	</mvc:annotation-driven>
 */
