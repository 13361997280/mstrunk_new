package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSONObject;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.service.CreditDataService;
import data.service.CreditMysqlDataService;
import data.service.RedisUnit;
import util.Signatures;

import java.util.Map;

public class BusService {
	public static final ESLogger logger = Loggers.getLogger(BusService.class);

	public Object doProcess(Map param) {
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		String message = "";
		String busName = "";
		try {
			busName = param.get("productType").toString();
			if(busName.equals("签到")) {
				int    userId = param.get("userId")==null?0:Integer.parseInt(param.get("userId").toString());
				boolean isSign = RedisUnit.isSign(userId);
				if(isSign){
					message = "用户今天已经签到!";
				}else{
					flag = Signatures.verify(param,"657y9b7faa6a50a4abb5e00b5117ca45");
					if(!flag)message = "签到签名失败!";
				}

			}
			else if(busName.equals("任务")) {
				int    taskId = param.get("taskId")==null?0:Integer.parseInt(param.get("taskId").toString());
				boolean isExistTask = RedisUnit.isValidTaskId(taskId);
				if(isExistTask) {
					flag = Signatures.verify(param, "1a5y8b7acc6a81a4abc5e90b5029c1fd");
					if (!flag) message = "任务签名失败!";
				}else{
					message = "任务不存在!";
				}
			}

			if(flag){
				logger.info("开始处理业务逻辑");
				CreditMysqlDataService.getInstance().doProcess(param);
			}
		} catch (Exception e) {
			message = busName+"-"+e.getMessage();
			e.printStackTrace();
		}
		jsonObject.put("success",flag);
		jsonObject.put("message",message);
		return jsonObject;
	}
}
