package com.qianwang.web.util.jsonresult;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianwang.web.util.jsonargs.JsonArgumentResolver;

/**
 * 
 * @author wangjg
 *
 */
public class MyRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {
	
	public MyRequestMappingHandlerAdapter() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(this.getJsonConverter());
		
		this.setMessageConverters(messageConverters);
		
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		argumentResolvers.add(new JsonArgumentResolver());
		this.setCustomArgumentResolvers(argumentResolvers);
		
	}

	private HttpMessageConverter getJsonConverter() {
		MappingJackson2HttpMessageConverter jsonConvert = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(df);
		jsonConvert.setObjectMapper(objectMapper);
		
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));
		supportedMediaTypes.add(new MediaType("application","json"));
		jsonConvert.setSupportedMediaTypes(supportedMediaTypes);
		return jsonConvert;
	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		try {
			HandlerMethodReturnValueHandlerComposite c = this.getFieldValue(this, "returnValueHandlers", HandlerMethodReturnValueHandlerComposite.class);
			List<HandlerMethodReturnValueHandler> rhList = this.getFieldValue(c, "returnValueHandlers", List.class);
			rhList.add(0, new ApiMethodReturnValueHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private <T> T getFieldValue(Object bean, String name, Class<T> type) throws Exception{
		Field field = ReflectionUtils.findField(bean.getClass(), name, type);
		field.setAccessible(true);
		T c = (T) field.get(bean);
		return c;
	}
}
