package com.qianwang.web.util.jsonresult;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.qianwang.util.json.JsonUtil;
import com.qianwang.web.result.AjaxResult;

/**
 * 
 * @author wangjg
 *
 */
public class ApiMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		Class<?> clazz = returnType.getDeclaringClass();
		Method method = returnType.getMethod();
		if(method.isAnnotationPresent(ApiResult.class)){
			return true;
		}
		if(clazz.isAnnotationPresent(ApiResult.class)){
			return true;
		}
		if(clazz.getSuperclass().isAnnotationPresent(ApiResult.class)){
			return true;
		}
		return false;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        writeInternal(returnValue, response);
	}
	
    protected void writeInternal(Object returnValue, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        
        AjaxResult r = AjaxResult.successResult(returnValue);
        
    	String json = JsonUtil.writeValueAsString(r);
    	byte[] buf = json.getBytes("utf-8");
        response.setContentLength(buf.length);
        OutputStream out = response.getOutputStream();
        out.write(buf);
        out.close();
    }

}

/*
		<mvc:return-value-handlers>
			<bean class="com.qbao.utils.springmvc.ApiMethodReturnValueHandler" />
		</mvc:return-value-handlers>
		
 */
/*
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    
    	<property name="returnValueHandlers">
			<list>
				<bean class="com.qianwang.web.util.jsonresult.ApiMethodReturnValueHandler" />
			</list>
		</property>

*/


