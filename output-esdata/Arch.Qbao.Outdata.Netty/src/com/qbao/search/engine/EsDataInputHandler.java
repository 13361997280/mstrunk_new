package com.qbao.search.engine;

import org.apache.commons.lang.StringUtils;

import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import data.service.DataInputEsService;
import data.service.QbaoLogInputService;
import util.JsonResult;

public class EsDataInputHandler extends SimpleHttpRequestHandler<Object> {

	private DataInputEsService esService = new DataInputEsService();

	private QbaoLogInputService qbaoService = new QbaoLogInputService();

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		String uri = httpRequest.getUri();
		if (uri.contains("api/v1.0/lbsDataInput")) {
			// lbs数据处理
			return JsonResult.success(esService.dealLbsDataInput()).getJsonStr();
		} else if (uri.contains("api/v1.0/touTiaoDataInput")) {
			// toutiao数据处理
			return JsonResult.success(esService.dealTouTiaoDataInput()).getJsonStr();
		} else if (uri.contains("api/old/qbaoLogDataInput")) {
			// qbaoLog数据处理 老库
			String startStr = CommonUtil.getParam(httpRequest, "start");
			String endStr = CommonUtil.getParam(httpRequest, "end");
			Long start = 0l, end = 0l;
			if (StringUtils.isNotEmpty(startStr)) {
				start = Long.parseLong(startStr);
			}
			if (StringUtils.isNotEmpty(endStr)) {
				end = Long.parseLong(endStr);
			}
			return JsonResult.success(esService.dealQbaoLogDataInput(start, end)).getJsonStr();
		} else if (uri.contains("api/v1.0/qbaoLogDataInput")) {
			// qbaoLog数据处理 新库
			return JsonResult.success(qbaoService.dealQbaoLogDataInput()).getJsonStr();
		}
		return null;
	}

}
