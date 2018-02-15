package com.qbao.search.rpc.netty;

import java.util.concurrent.ExecutorService;

import org.jboss.netty.handler.codec.http.HttpRequest;



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
 * @Create-at 2011-8-12 13:14:18
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-12 13:14:18  	li_yao		1.0			Newly created
 */
public interface HttpRequestParser {
	
	HttpRequestHandler parse(HttpRequest request) throws Exception;
	
	ExecutorService getExecutor(Class<? extends HttpRequestHandler> handlerClass);
	
}
