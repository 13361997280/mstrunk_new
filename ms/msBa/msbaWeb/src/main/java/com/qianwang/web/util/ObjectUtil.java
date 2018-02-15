package com.qianwang.web.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectUtil {

	private static final Log log = LogFactory.getLog(ObjectUtil.class);
	/**
	 * 根据属性名获取属性值，不适用于boolean属性
	 * @param obj
	 * @param paramName
	 * @return
	 */
	public static Object getParameterByName(Object obj, String paramName){
		Object _value = null;
		try {
			//加载类
			Class _class = Class.forName(obj.toString().split("@")[0]);
			//获得对象方法集合
			Field[] fields = _class.getDeclaredFields();
			String fdname=null;
			Method metd = null;
			for (Field field : fields) {
				fdname = field.getName();// 得到字段名
				if(fdname.equals(paramName)){
					metd = _class.getMethod("get" + change(fdname), null);
					_value = metd.invoke(obj, null);
				}
			}
		} catch (Exception e) {
			log.error("Error in getting parmater value.", e);
		}
		return _value;
	}
	
	public static String change(String src) {  
        if (src != null) {  
            StringBuffer sb = new StringBuffer(src);  
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));  
            return sb.toString();  
        } else {  
            return null;  
        }  
    }
}
