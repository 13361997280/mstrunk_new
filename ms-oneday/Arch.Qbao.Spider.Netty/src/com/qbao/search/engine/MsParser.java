package com.qbao.search.engine;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.rpc.netty.HttpRequestHandler;
import com.qbao.search.rpc.netty.RestParser;

public class MsParser extends RestParser {

	public MsParser() {
		//提供内部调用
		addHandler(HttpMethod.GET, "/process", EsHandler.class);
		addHandler(HttpMethod.GET, "/toutiao", ToutiaoHandle.class);
		addHandler(HttpMethod.HEAD, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.GET, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.POST, "/monitor.jsp", MonitorHandler.class);
	}

	@Override
	public HttpRequestHandler parse(HttpRequest httpRequest) throws Exception {
		HttpRequestHandler handler = super.parse(httpRequest);
		return handler;
	}
}
