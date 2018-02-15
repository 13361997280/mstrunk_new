package com.qbao.search.rpc.netty;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;


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
 * @Create-at 2011-8-12 13:31:09
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-12 13:31:09  	li_yao		1.0			Newly created
 */
public interface HttpResponseWrapper {
	
	HttpResponse wrap(HttpRequest httpRequest, Object response);
	
}
