package com.qbao.search.rpc;

import java.io.IOException;
import java.util.Map;

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
 * @Create-at 2011-8-5 09:58:56
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 09:58:56  	li_yao		1.0			Newly created
 */
public interface Server {
	
	void init()throws Exception;

	/**
	 * @param port
	 * @throws IOException
	 */
	void start(int port) throws IOException;
		
	/**
	 * @param port
	 * @param tryMode
	 * 		  if true will try binding _port which will be increment until succeed.
	 * 		  otherwise,only bind once with the specified port
	 * @return the succeed bound port number
     */
	void start(int port, boolean tryMode)throws IOException;
				
	int getPort();
	
	void stop() throws Exception;
	
	void getMonitorInfos(String prefix, Map<String, Object> map);

}
