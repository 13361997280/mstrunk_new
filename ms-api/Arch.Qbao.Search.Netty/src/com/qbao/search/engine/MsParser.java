package com.qbao.search.engine;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.rpc.netty.HttpRequestHandler;
import com.qbao.search.rpc.netty.RestParser;

public class MsParser extends RestParser {

	public MsParser() {
		//Command for netty，es re-index ,dailymail,startup,monitor...
		addHandler(HttpMethod.GET, "/command", CmdHandler.class);
		
		// commons
		addHandler(HttpMethod.GET, "/api/v1.0/token", OpenApiHandler.class);
		
		//提供外部调用
		addHandler(HttpMethod.GET, "/api/v1.0/getEntityByUserId", OpenApiHandler.class);
		addHandler(HttpMethod.GET, "/api/v1.0/multySearchForEntity", OpenApiHandler.class);
		//提供内部调用
		addHandler(HttpMethod.GET, "/dict", EsHandler.class);
		addHandler(HttpMethod.GET, "/getUserIdsById", EsHandler.class);
		addHandler(HttpMethod.GET, "/updateUserId", EsHandler.class);
		addHandler(HttpMethod.GET, "/calLabel", EsHandler.class);
		addHandler(HttpMethod.GET, "/getEntityByUserId", EsHandler.class);
		addHandler(HttpMethod.GET, "/multySearchForEntity", EsHandler.class);
		addHandler(HttpMethod.GET, "/getSearchConstForFront", EsHandler.class);
		addHandler(HttpMethod.GET, "/saveLog", EsHandler.class);
		addHandler(HttpMethod.GET, "/getActiveUserCount", EsHandler.class);
		addHandler(HttpMethod.GET, "/getProperties", EsHandler.class);

		addHandler(HttpMethod.HEAD, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.GET, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.POST, "/monitor.jsp", MonitorHandler.class);

		addHandler(HttpMethod.GET, "/getLog", EsHandler.class);
	}

	@Override
	public HttpRequestHandler parse(HttpRequest httpRequest) throws Exception {
		HttpRequestHandler handler = super.parse(httpRequest);
		return handler;
	}
}
