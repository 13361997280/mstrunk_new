package com.qbao.search.engine;

import com.qbao.search.engine.service.NewsService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import java.util.Map;

public class NewsHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(NewsHandler.class);

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = null;
		NewsService service = new NewsService();
		String uri = httpRequest.getUri();
		Map param = CommonUtil.getParamMap(httpRequest);
		try {
			 if(uri.contains("newslist")) {
				htmlFrame = service.getNewsList(param);
			 }else if(uri.contains("categorylist")) {
				 htmlFrame = service.categorylist(param);
			 }else if(uri.contains("savenewsids")) {
				 htmlFrame = service.saveNewsids(param);
			 }else if(uri.contains("newsdetail")) {
				 htmlFrame = service.getNewsdetail(param);
			 }else if(uri.contains("savenewstype")) {
				 htmlFrame = service.saveNewsType(param);
			 }else if(uri.contains("newstypelist")) {
				 htmlFrame = service.newstypelist(param);
			 }
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++NewsHandler error!+++", e);
		}
		
		return htmlFrame;
	}
}
