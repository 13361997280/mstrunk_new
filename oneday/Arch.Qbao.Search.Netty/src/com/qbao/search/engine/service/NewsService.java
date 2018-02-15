package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSONObject;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.service.NewsDataService;
import po.NewsDetailPo;

import java.util.Map;

public class NewsService {
	public static final ESLogger logger = Loggers.getLogger(NewsService.class);

	public JSONObject getNewsList(Map map){
		return NewsDataService.getInstance().searchList(map);
	}
	public JSONObject categorylist(Map map){
		return NewsDataService.getInstance().categorylist((String)map.get("userId"));
	}
	public JSONObject newstypelist(Map map){
		return NewsDataService.getInstance().newsTypelist((String)map.get("userId"));
	}
	public JSONObject saveNewsids(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			NewsDataService.getInstance().saveNewsIds(map);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}
	public JSONObject saveNewsType(Map map){
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			NewsDataService.getInstance().saveUserNewsType(map);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		return jsonObject;
	}
	public JSONObject getNewsdetail(Map map){
		NewsDetailPo newsDetailPo = NewsDataService.getInstance().searchNewsDetail((String)map.get("newsId"),(String)map.get("userId"),(String)map.get("page"));
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(newsDetailPo);
		return jsonObject;
	}
}
