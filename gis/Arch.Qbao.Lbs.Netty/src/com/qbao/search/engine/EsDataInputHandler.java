package com.qbao.search.engine;

import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;

import data.service.DataInputEsService;
import util.JsonResult;

public class EsDataInputHandler extends SimpleHttpRequestHandler<Object> {

	private DataInputEsService esService = new DataInputEsService();

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		String uri = httpRequest.getUri();
		if (uri.contains("api/v1.0/lbsDataInput")) {
			// lbs数据处理
			return JsonResult.success(esService.dealLbsDataInput()).toString();
		} else if (uri.contains("api/v1.0/touTiaoDataInput")) {
			// toutiao数据处理
			return JsonResult.success(esService.dealTouTiaoDataInput()).toString();
		} else if (uri.contains("api/v1.0/qbaoLogDataInput")) {
			// qbaoLog数据处理
			return JsonResult.success(esService.dealQbaoLogDataInput()).toString();
		}
		return null;
	}

}
