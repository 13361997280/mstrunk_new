package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSONObject;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.service.BaDataService;
import data.service.CreditDataService;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.util.internal.StringUtil;

import java.util.Map;

public class BaService {
	public static final ESLogger logger = Loggers.getLogger(BaService.class);

	public JSONObject search(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			JSONObject data = BaDataService.getInstance().search(map);
			if(data.get("message")!=null){
				jsonObject.put("message",data.get("message"));
			}else{
				jsonObject.put("data",data);
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}
	public JSONObject searchDetailUp(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			JSONObject data = BaDataService.getInstance().searchDetailUp(map);
			if(data.get("message")!=null){
				jsonObject.put("message",data.get("message"));
			}else{
				jsonObject.put("data",data);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}
	public JSONObject searchDetailDown(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			JSONObject data = BaDataService.getInstance().searchDetailDown(map);
			if(data.get("message")!=null){
				jsonObject.put("message",data.get("message"));
			}else{
				jsonObject.put("data",data);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}
	public JSONObject addScoreList(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			JSONObject data = BaDataService.getInstance().searchAddScoreDetailList(map);
			if(data.get("message")!=null){
				jsonObject.put("message",data.get("message"));
			}else{
				jsonObject.put("data",data);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}
	public JSONObject saveAjustData(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {

			String msg = BaDataService.getInstance().saveAjustData(map);
			if(StringUtils.isEmpty(msg)) {
				flag = true;
			}else{
				jsonObject.put("message",msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}

}
