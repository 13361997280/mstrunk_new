package com.qbao.search.rpc;
/**
 * 
 * <br>
 * Copyright 2012 Ctrip.com, Inc. All rights reserved.<br>
 * Ctrip.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *<br>
 * Projet Name:	Arch.Search.Common<br>
 * File Name:	RequestHandler.java<br>
 *
 * Create Date: 2012-1-10<br>
 * Version:		1.0<br>
 * Modification:<br><br>
 */
public interface RequestHandler extends Runnable {
	
	void setServer(Server server);
	
	void setRequest(Object request)throws Exception;
	
	void setTimeStamp(long timeStamp);
	
	long getTimeStamp();
	
}
