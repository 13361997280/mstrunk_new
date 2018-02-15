package com.qbao.search.engine;

import com.alibaba.fastjson.JSONObject;
import com.qbao.search.engine.service.EsService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import java.util.Map;

public class EsHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(EsHandler.class);

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = null;
		EsService service = new EsService();
		String uri = httpRequest.getUri();
		try {
			if(uri.contains("process")) {
				htmlFrame = service.process();
			}
			return htmlFrame;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++EsHandler error!+++", e);
		}
		
		return htmlFrame;
	}
}
