package com.qbao.search.engine;

import com.qbao.search.engine.handler.BusItemHandler;
import com.qbao.search.rpc.netty.HttpRequestHandler;
import com.qbao.search.rpc.netty.RestParser;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class CreditParser extends RestParser {

	public CreditParser() {
		//Command for netty，es re-index ,dailymail,startup,monitor...
		addHandler(HttpMethod.GET, "/command", CmdHandler.class);


		//提供内部调用

		addHandler(HttpMethod.HEAD, "/monitor.jsp", MonitorHandler.class);
		addHandler
				(HttpMethod.GET, "/monitor.jsp", MonitorHandler.class);
		addHandler(HttpMethod.POST, "/monitor.jsp", MonitorHandler.class);

		//业务接口
		addHandler(HttpMethod.GET, "/bus/log", BusHandler.class);//埋点接口
		addHandler(HttpMethod.POST, "/bus/log", BusHandler.class);//埋点接口
		addHandler(HttpMethod.GET, "/bus/ratio", ScoreHandler.class);//行为系数接口
		addHandler(HttpMethod.GET, "/score", ScoreHandler.class);//信用总分及系数
		addHandler(HttpMethod.GET,"/bus/list", BusItemHandler.class);//加分项接口

		//后台查询接口
		addHandler(HttpMethod.GET, "/user/list", BaHandler.class);//用户信用信息查询接口
		addHandler(HttpMethod.GET, "/user/detail", BaHandler.class);//用户信用信息详情接口
		addHandler(HttpMethod.GET, "/user/detaiList", BaHandler.class);//用户信用信息详情接口
		addHandler(HttpMethod.GET, "/user/addScoreList", BaHandler.class);//加分明细查询接口
		addHandler(HttpMethod.GET, "/user/adjust", BaHandler.class);//人工调整接口
		addHandler(HttpMethod.POST, "/user/adjust", BaHandler.class);//人工调整接口


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
