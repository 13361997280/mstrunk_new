package com.qbao.search.engine;

import com.qbao.search.engine.service.BaService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import java.util.Map;

public class BaHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(BaHandler.class);

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = null;
		BaService service = new BaService();
		String uri = httpRequest.getUri();
		Map param = CommonUtil.getParamMap(httpRequest);
		try {
			 if(uri.contains("/user/list")) {
				htmlFrame = service.search(param);
			 }else if(uri.contains("/user/detail")) {
				 htmlFrame = service.searchDetailUp(param);
			 }else if(uri.contains("/user/detaiList")) {
				 htmlFrame = service.searchDetailDown(param);
			 }else if(uri.contains("/user/addScoreList")) {
				 htmlFrame = service.addScoreList(param);
			 }else if(uri.contains("/user/adjust")) {
				 htmlFrame = service.saveAjustData(param);
			 }
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++BaHandler error!+++", e);
		}
		
		return htmlFrame;
	}
}
