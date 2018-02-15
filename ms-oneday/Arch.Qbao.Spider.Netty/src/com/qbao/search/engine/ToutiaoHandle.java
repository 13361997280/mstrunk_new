package com.qbao.search.engine;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import com.qbao.search.engine.service.EsService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;

import po.ToutiaoCmdPo;
import quartz.ToutiaoSpiderHealthMonitorTask;

public class ToutiaoHandle extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(ToutiaoHandle.class);

	public ToutiaoCmdPo getParms() throws UnsupportedEncodingException {

		ToutiaoCmdPo toutiaoCmdPo = new ToutiaoCmdPo(httpRequest);

		return toutiaoCmdPo;

	}
	
	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = "";
		ToutiaoCmdPo toutiaoCmdPo = getParms();
		try {
			switch (toutiaoCmdPo.getCmd()) {
			case "status":		
				htmlFrame = ToutiaoSpiderHealthMonitorTask.getInstance().SpiderStatusMapPrint();
				break;

			default:
				return " command url un match , valid, check url again! ";
				//break;
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("+++EsHandler error!+++", e);
		}
		
		return htmlFrame;
	}
}
