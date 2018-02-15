package com.qbao.search.engine;

import com.qbao.search.engine.service.BusService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import java.util.Map;

public class BusHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(BusHandler.class);

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = null;
		BusService service = new BusService();
		String uri = httpRequest.getUri();
		Map param = CommonUtil.getParamMap(httpRequest);
		try {
			 if(uri.contains("/bus/log")) {
				htmlFrame = service.doProcess(param);
			 }
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++NewsHandler error!+++", e);
		}
		
		return htmlFrame;
	}
}
