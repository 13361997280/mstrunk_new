package com.qbao.search.engine;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.qbao.search.engine.service.EsService;
import com.qbao.search.engine.service.OpenApiService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import util.SimpleResult;
import util.TokenUtils;
import vo.OpenApiPo;

public class OpenApiHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(OpenApiHandler.class);

	public OpenApiPo getParms() throws UnsupportedEncodingException {

		OpenApiPo openApiPo = new OpenApiPo(httpRequest);

		return openApiPo;

	}

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		Object htmlFrame = "null";
		OpenApiPo openApiPo = getParms();
		OpenApiService service = new OpenApiService();
		EsService esService = new EsService();
		String uri = httpRequest.getUri();
		String token = CommonUtil.getParam(httpRequest,"token");
		try {
			if(uri.contains("api/v1.0/token")) { 
				htmlFrame =  service.getToken(openApiPo);
			}else {
				SimpleResult result = TokenUtils.getInstance().decodeToken(token);
				if (result.isSuccess()) {
					@SuppressWarnings("rawtypes")
					Map param = CommonUtil.getParamMap(httpRequest);
					param.remove("appid");
					param.remove("secret");
					param.remove("token");
					if (uri.contains("api/v1.0/getEntityByUserId")) {
						htmlFrame = esService.getEntityByUserId(param);
					} else if (uri.contains("api/v1.0/multySearchForEntity")) {
						htmlFrame = esService.multySearchForEntity(param);
					}
				}else {
					htmlFrame = result.getData();
				}
			}
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++OpenApiHandler error!+++", e);
			htmlFrame = "token invalid";
		}
		
		return htmlFrame;
	}

}
