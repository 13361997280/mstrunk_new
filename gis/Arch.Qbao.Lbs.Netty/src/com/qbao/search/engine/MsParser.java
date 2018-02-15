package com.qbao.search.engine;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.rpc.netty.HttpRequestHandler;
import com.qbao.search.rpc.netty.RestParser;

public class MsParser extends RestParser {

	public MsParser() {
		/**Command for netty，es re-index ,dailymail,startup,monitor...**/
		addHandler(HttpMethod.GET, "/command", CmdHandler.class);
		
		
		/**提供外部调用**/
		addHandler(HttpMethod.GET, "/api/v1.0/addLocationInfo", PoiInputHandler.class);
		addHandler(HttpMethod.POST, "/api/v1.0/addLocationInfoList", PoiInputHandler.class);
		
		/**es数据导出到kafka*/
		addHandler(HttpMethod.POST, "/api/v1.0/lbsDataInput", EsDataInputHandler.class);
		addHandler(HttpMethod.POST, "/api/v1.0/touTiaoDataInput", EsDataInputHandler.class);
		addHandler(HttpMethod.POST, "/api/v1.0/qbaoLogDataInput", EsDataInputHandler.class);


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
