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
 * @Create-at 2011-8-23 16:32:12
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-23 16:32:12  	li_yao		1.0			Newly created
 */
public abstract class AbstractRequest implements Request {
	
	protected long timeStamp;
	

	@Override
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public long getTimeStamp() {
		return timeStamp;
	}


}
