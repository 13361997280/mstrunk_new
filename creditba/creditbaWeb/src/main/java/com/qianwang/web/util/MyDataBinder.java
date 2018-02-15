package com.qianwang.web.util;

import java.beans.PropertyEditor;
import java.util.Date;

import org.springframework.validation.DataBinder;

/**
 * 
 * @author wangjg
 *
 */
public class MyDataBinder extends DataBinder {

	public MyDataBinder(Object target) {
		super(target);
		registerCommonEditor(this);
	}
	
	public static void registerCommonEditor(DataBinder dataBinder){
		dataBinder.registerCustomEditor(Date.class, new MyDateEditor());
	}
	
	@Override
	public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
		PropertyEditor pe = super.findCustomEditor(requiredType, propertyPath);
		
		return pe;
	}
}
