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
		Map param = CommonUtil.getParamMap(httpRequest);
		try {
			if(uri.contains("dict")) {
				htmlFrame = service.dict();
			}else if(uri.contains("getUserIdsById")) {
				htmlFrame = service.getUserIdsById(param);
			}else if(uri.contains("updateUserId")) {
				htmlFrame = service.updateUserId(param);
			}else if(uri.contains("calLabel")) {
				htmlFrame = service.calLabel(param);
			}else if(uri.contains("getEntityByUserId")) {
				htmlFrame = service.getEntityByUserId(param);
			}else if(uri.contains("multySearchForEntity")) {
				htmlFrame = service.multySearchForEntity(param);
			}else if(uri.contains("getSearchConstForFront")) {
				htmlFrame = service.getSearchConstForFront(param);
			}else if(uri.contains("saveLog")) {
				htmlFrame = service.saveLog(param);
			}else if(uri.contains("getProperties")) {
				htmlFrame = service.getProperties(param);
			}else if(uri.contains("getActiveUserCount")) {
				htmlFrame = service.getActiveUserCount();
			}else if(uri.contains("getLog")) {
				htmlFrame = service.getLog(param);
			}
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++EsHandler error!+++", e);
		}
		
		return htmlFrame;
	}
}
