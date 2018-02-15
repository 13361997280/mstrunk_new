package com.qbao.search.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.engine.service.ScoreService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.EditResponseHttpHandler;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class ScoreHandler extends EditResponseHttpHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(ScoreHandler.class);

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = null;
		ScoreService service = new ScoreService();
		String uri = httpRequest.getUri();
		Map param = CommonUtil.getParamMap(httpRequest);
		Integer userId = Integer.parseInt((String) param.get("userId"));
		String jsoncallback = (String)param.get("jsoncallback");
		try {
			if(uri.contains("/score")) {
				htmlFrame = service.getTotalScore(userId);
				if(jsoncallback!=null && jsoncallback.length()>0){
					htmlFrame = this.getJsonpString(htmlFrame, jsoncallback);
				}
			}else if(uri.contains("/bus/ratio")){
				htmlFrame = service.getRatio(userId);
			}
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++NewsHandler error!+++", e);
		}

		return htmlFrame;
	}

	private String getJsonpString(Object data, String jsoncallback){
		String json = JSONObject.toJSONStringWithDateFormat(data, "yyyy-MM-dd HH:mm:ss");
		String jsonp = jsoncallback+'('+json+");";
		return jsonp;
	}

	@Override
	protected void editResponse(HttpResponse response) {
		response.setHeader("Content-Type", "application/json; charset=UTF-8");

		//支持跨域请求
		response.addHeader("access-control-allow-origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS,POST,GET");
		response.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
	}
}
