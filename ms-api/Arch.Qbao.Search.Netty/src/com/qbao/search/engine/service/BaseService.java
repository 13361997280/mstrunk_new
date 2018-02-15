package com.qbao.search.engine.service;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import vo.OpenApiPo;

public abstract class BaseService {
	public static final ESLogger logger = Loggers.getLogger(BaseService.class);

	public abstract String get(OpenApiPo openApiPo);

	public String paserListJson(Object list) {

		JSONArray json = JSONArray.fromObject(list);

		return json.toString();
	}

	public String paserObject(Object obj) {

		JSONObject json = JSONObject.fromObject(obj);

		return json.toString();

	}

}
