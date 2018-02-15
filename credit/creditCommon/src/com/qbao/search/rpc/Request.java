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
 * @Create-at 2011-8-5 09:58:43
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 09:58:43  	li_yao		1.0			Newly created
 */
public interface Request{
	
	/***************************************************************
	 * @method： setTimeStamp<br>
	 * <br>
	 * @param timeStamp<br>
	 * <br>
	 */
	void setTimeStamp(long timeStamp);
	
	
	/***************************************************************
	 * @method： getTimeStamp<br>
	 * <br>
	 * @return<br>
	 * <br>
	 */
	long getTimeStamp();
	
}
