package com.qbao.search.engine.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.qbao.search.conf.LoadConfig;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import data.db.IndexUpdate;
import net.sf.json.JSONObject;
import redis.RedisConst;
import redis.RedisUtil;
import util.TokenUtils;
import vo.CommandPo;
import vo.OpenApiPo;

/**
 * 各种netty，es re-index ,dailymail,startup, restart, shutdown,monitor,health
 * check，stat..
 **/
public class CommandService implements Closeable {
	
	public static final ESLogger logger = Loggers.getLogger(CommandService.class);

	
	private RedisUtil redisUtil = new RedisUtil();
	
    /**
     * 
     * @param apiPo
     * @return
     */
	public String IndexUpdate(CommandPo po,boolean fileBackup) {
		try { 
			String taskLog = IndexUpdate.getInstance().DoTask(po.getDate(),fileBackup);
			return "<html>"+taskLog+"</html>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "unknow error! 400";
	}
	
	public String IndexStop(CommandPo po) {
		try { 
			String taskLog = IndexUpdate.getInstance().StopTask();
			return "<html>"+taskLog+"</html>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "unknow error! 404";
	}
	
	

	@Override
	public void close() throws IOException {
	}
}
