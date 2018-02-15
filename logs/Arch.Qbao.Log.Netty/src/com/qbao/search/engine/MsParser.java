package com.qbao.search.engine;

import com.qbao.search.engine.qbaolog.LogHandler;
import com.qbao.search.engine.qbaolog.SearchHandler;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.rpc.netty.HttpRequestHandler;
import com.qbao.search.rpc.netty.RestParser;

public class MsParser extends RestParser {

	public MsParser() {

		addHandler(HttpMethod.GET, "/compass/log", LogHandler.class);
		addHandler(HttpMethod.POST, "/compass/log", LogHandler.class);

		addHandler(HttpMethod.GET, "/qbaolog/search", SearchHandler.class);

		addHandler(HttpMethod.HEAD, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.GET, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.POST, "/monitor.jsp", MonitorHandler.class);
	}

	@Override
	public HttpRequestHandler parse(HttpRequest httpRequest) throws Exception {
		if(httpRequest.getUri().contains("%22")) {
			httpRequest.setUri("/favicon.ico");
		}
		HttpRequestHandler handler = super.parse(httpRequest);
		return handler;
	}
}
