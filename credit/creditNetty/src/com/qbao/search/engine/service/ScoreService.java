package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSONObject;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.service.CreditMysqlDataService;

import java.util.Map;

public class ScoreService {
	public static final ESLogger logger = Loggers.getLogger(ScoreService.class);

	public Map getTotalScore(Integer userId){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		int     returnCode = 0;
		String  message = "";
		JSONObject result = new JSONObject();
		try {
			result = CreditMysqlDataService.getInstance().getTotalScore(userId);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonObject.put("success",flag);
		jsonObject.put("data",result);
		jsonObject.put("returnCode",returnCode);
		jsonObject.put("message",message);
		return jsonObject;
	}
	public Map getRatio(Integer userId){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		int     returnCode = 0;
		String  message = "";
		JSONObject result = new JSONObject();
		try {
			result = CreditMysqlDataService.getInstance().getRatio(userId);
			if(result!=null)flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonObject.put("success",flag);
		jsonObject.put("data",result);
		jsonObject.put("returnCode",returnCode);
		jsonObject.put("message",message);
		return jsonObject;
	}

}
