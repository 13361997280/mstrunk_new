/**
 * 
 */
package com.qbao.search.rpc.netty;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

/**
 * 
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
 * @Create-at 2011-12-19 20:20:47
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-19 20:20:47  	li_yao		1.0			Newly created
 */
public class RestParser implements HttpRequestParser {
	private static ESLogger logger = Loggers.getLogger(RestParser.class);

	final protected Map<HttpMethod, 
			Map<String, Class<? extends HttpRequestHandler>>> 
		handlerClasses = new HashMap<HttpMethod,
			Map<String, Class<? extends HttpRequestHandler>>>();
	
	public RestParser(){
		addHandler(HttpMethod.GET, "/favicon.ico", EmptyHandler.class);
		addHandler(HttpMethod.PUT, "/chmodor", ChmodOtherReadableHandler.class);
	}
	
	@Override
	public HttpRequestHandler parse(HttpRequest httpRequest) 
			throws Exception{
		Map<String, Class<? extends HttpRequestHandler>> subHandlerClasses = 
			handlerClasses.get(httpRequest.getMethod());
		if(subHandlerClasses == null){
			throw new IllegalAccessException("Illegal http method: " + 
					httpRequest.getMethod());
		}
		String uri = HttpUtil.getUri(httpRequest.getUri().toLowerCase());
		
		accessCheck(uri, httpRequest.getHeader(HttpServer.CLIENT_IP_HEADER));
		
		Class<? extends HttpRequestHandler> handlerClass = 
			subHandlerClasses.get(uri);
		if(handlerClass == null){
			throw new IllegalAccessException("Illegal http request:[method]"
				+ httpRequest.getMethod() + ",[uri]" + httpRequest.getUri());
		}
		return handlerClass.newInstance();
	}
	
	void accessCheck(String uri, String clientIP) throws IllegalAccessException {
		String aclRules = Config.get().get("acl.rules");
		if(aclRules == null || aclRules.isEmpty()) {
			return;
		}
		for(String aclRule:aclRules.split("#@@@#")) {
			String[] rule = aclRule.split("#~~~#");
			if(uri.matches(rule[0])) {
				if(!clientIP.matches(rule[1])) {
					throw new IllegalAccessException("The IP:" + clientIP
						+ " can't access the URI:" + uri);
				}
			}
		}
	}
	
	public synchronized void addHandler(HttpMethod method, String uri,
			Class<? extends HttpRequestHandler> handlerClass){
		uri = uri.toLowerCase();
		Map<String, Class<? extends HttpRequestHandler>> subHandlerClasses =
			handlerClasses.get(method);
		if(subHandlerClasses == null){
			subHandlerClasses = 
				new HashMap<String, Class<? extends HttpRequestHandler>>();
			handlerClasses.put(method, subHandlerClasses);
		}
		if(uri.charAt(uri.length() - 1) == '/'){
			uri = uri.substring(0, uri.length() - 1);
		}
		Class<? extends HttpRequestHandler> cls = subHandlerClasses.get(uri);
		if(cls != null){
			throw new IllegalArgumentException("handler:[" + method + " " +
					uri + " " + cls + "] has already existed");
		}
		
		subHandlerClasses.put(uri, handlerClass);
		logger.info("add handler:[{} {} {}]", method, uri, handlerClass);
	}

	@Override
	public ExecutorService getExecutor(Class<? extends HttpRequestHandler> 
		handlerClass) {
		// TODO Auto-generated method stub
		return null;
	}

}
