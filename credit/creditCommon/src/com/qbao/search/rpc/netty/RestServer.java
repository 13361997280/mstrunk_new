/**
 * 
 */
package com.qbao.search.rpc.netty;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

/**
 * @author li_yao
 *
 */
public class RestServer extends HttpServer {
	final static ESLogger logger = Loggers.getLogger(RestServer.class);
	
	final protected RestParser restParser = new RestParser();

	/**
	 * @param serverName
	 * @param sendException
	 * @param downloadRootPath
	 */
	public RestServer(String serverName, boolean sendException) {
		super(serverName, sendException);
	}

	public void addHandler(HttpMethod method, String uri,
			Class<? extends HttpRequestHandler> handlerClass){
		restParser.addHandler(method, uri, handlerClass);
	}

	@Override
	final protected HttpRequestParser getHttpRequestParser() {
		return restParser;
	}

}
