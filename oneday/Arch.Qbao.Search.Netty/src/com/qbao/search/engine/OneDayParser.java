package com.qbao.search.engine;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.rpc.netty.HttpRequestHandler;
import com.qbao.search.rpc.netty.RestParser;

public class OneDayParser extends RestParser {

	public OneDayParser() {
		//Command for netty，es re-index ,dailymail,startup,monitor...
		addHandler(HttpMethod.GET, "/command", CmdHandler.class);


		//提供内部调用

		addHandler(HttpMethod.HEAD, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.GET, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.POST, "/monitor.jsp", MonitorHandler.class);

		//新闻资讯
		addHandler(HttpMethod.GET, "/newslist", NewsHandler.class);
		addHandler(HttpMethod.GET, "/savenewsids", NewsHandler.class);
		addHandler(HttpMethod.GET, "/categorylist", NewsHandler.class);
		addHandler(HttpMethod.GET, "/newsdetail", NewsHandler.class);
		addHandler(HttpMethod.GET, "/savenewstype", NewsHandler.class);
		addHandler(HttpMethod.GET, "/newstypelist", NewsHandler.class);

		addHandler(HttpMethod.GET,"/compass/index",CompassHandler.class);
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
