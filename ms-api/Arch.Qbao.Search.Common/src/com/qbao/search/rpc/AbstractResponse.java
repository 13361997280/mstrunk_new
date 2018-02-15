package com.qbao.search.rpc;

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
 * @Create-at 2011-8-29 15:46:54
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-29 15:46:54  	li_yao		1.0			Newly created
 */
public abstract class AbstractResponse implements Response {
	
	protected String msg;
	
	/***************************************************************
	 * @Constructor： AbstractResponse.java - AbstractResponse<br>
	 * <br>
	 * @param msg<br>
	 * <br>
	 */
	public AbstractResponse(String msg){
		this.msg = msg;
	}
	
	
	/***************************************************************
	 * @method： toString<br>
	 * (non-Javadoc)<br>
	 * @see java.lang.Object#toString()<br>
	 * toString - AbstractResponse<br>
	 * <br>
	 */
	@Override
	public String toString(){
		return msg;
	}

}
