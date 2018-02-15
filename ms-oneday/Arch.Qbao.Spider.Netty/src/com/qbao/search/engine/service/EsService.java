package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSONObject;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import data.spider.ToutiaoApiProcesser;

public class EsService {
	public static final ESLogger logger = Loggers.getLogger(EsService.class);

	/**
	 * 爬虫程序开始
	 * @return
     */
	public JSONObject process() {
		ToutiaoApiProcesser.getInstance().doProcess();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success",true);
		return jsonObject;
	}
}
