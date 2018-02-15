package com.qbao.search.util;

import java.io.Serializable;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2011-8-19 09:14:28
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-19 09:14:28  	li_yao		1.0			Newly created
 */
public class StringEntry implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	public String name;
	
	public String value;
	
	public StringEntry(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public StringEntry(String name) {
		this(name, null);
	}
	
	@Override
	public String toString(){
		return new StringBuilder().append("[name:").append(name).append(
			", value:").append(value).append("]").toString();
	}
	
	@Override
	public StringEntry clone() {
		try {
			return (StringEntry) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
	}

}
