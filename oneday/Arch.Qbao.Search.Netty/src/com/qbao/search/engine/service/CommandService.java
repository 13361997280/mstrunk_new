package com.qbao.search.engine.service;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.db.IndexUpdate;
import vo.CommandPo;

import java.io.Closeable;
import java.io.IOException;

/**
 * 各种netty，es re-index ,dailymail,startup, restart, shutdown,monitor,health
 * check，stat..
 **/
public class CommandService implements Closeable {
	
	public static final ESLogger logger = Loggers.getLogger(CommandService.class);

    /**
     * 
     * @param po
     * @param fileBackup
     * @return
     */
	public String IndexUpdate(CommandPo po,boolean fileBackup) {
		try { 
			String taskLog = IndexUpdate.getInstance().DoTask(po,fileBackup);
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
