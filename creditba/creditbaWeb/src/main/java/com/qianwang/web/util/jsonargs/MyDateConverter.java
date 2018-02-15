package com.qianwang.web.util.jsonargs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;

/**
 * 把String转换成java.util.Date类型的类型转换器
 * @author Administrator
 *
 */
public class MyDateConverter implements Converter{
	private String full_timeformat = "yyyy-MM-dd HH:mm:ss SSS";

    @SuppressWarnings("rawtypes")
    public Object convert(Class type, Object value) {
        if(value == null){
            return null;
        }
        if(!(value instanceof String)){
            return value;
        }
        
        if(value.equals("")){
        	return null;
        }
        
		String formtText = full_timeformat.substring(0, ((String)value).length());
		DateFormat format = new SimpleDateFormat(formtText);
        try {
            return format.parse((String)value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}