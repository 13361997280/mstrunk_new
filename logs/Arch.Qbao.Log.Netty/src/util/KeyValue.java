package util;

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
 * @Modification-history <br>
 *                       Date Author Version Description <br>
 *                       ------------------------------------------------------
 *                       ---- <br>
 *                       2011-8-19 09:14:28 li_yao 1.0 Newly created
 */
public class KeyValue implements Serializable, Cloneable {

	static final long serialVersionUID = -7063686274853138531L;
	private Object key;
	private Object value;

	public KeyValue(Object key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
