package com.qianwang.web.util;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author wangjg
 *
 */
public class MyDateEditor extends PropertyEditorSupport {
	private String full_timeformat = "yyyy-MM-dd HH:mm:ss SSS";
	
	public void setAsText(String value) {
		if(value==null || value.isEmpty()){
			this.setValue(null);
			return;
		}
		String formtText = full_timeformat.substring(0, value.length());
		DateFormat format = new SimpleDateFormat(formtText);
		try {
			this.setValue(format.parse(value));
		} catch (ParseException e) {
		}
	}

	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? new SimpleDateFormat(full_timeformat).format(value) : "");
	}

}
