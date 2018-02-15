package com.qbao.search.rpc.netty;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.ScheduleServer;
import com.qbao.search.util.CommonUtil;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2011-12-6 14:54:24
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-6 14:54:24  	li_yao		1.0			Newly created
 */
public abstract class AbstractScheduleServer 
		extends RestServer implements ScheduleServer<String, String>{
	final ESLogger logger;	 
	private ScheduledExecutorService service=null;
	private AtomicInteger stopCounter = new AtomicInteger();
	
	private boolean running = false;
	
	private String lastSchedule = "";
	
	String lastRunTime = null;

	/**
	 * @param serverName
	 * @param sendException
	 */
	public AbstractScheduleServer(String serverName, boolean sendException
			, String indexName) {
		super(serverName, sendException);
		logger = Loggers.getLogger(this, indexName);
		
		//addHandler(HttpMethod.GET, "/stop", StopHandler.class);
		addHandler(HttpMethod.GET, "/monitor", MonitorHandler.class);
		
		addHandler(HttpMethod.GET, 
			"/" + indexName + "/startSchedule/", StartScheduleHandler.class);
		
		addHandler(HttpMethod.GET, 
				"/" + indexName + "/stopSchedule/", StopScheduleHandler.class);

	}
	
	final protected synchronized Object syncRunTask() throws Exception{
		if(stopCounter.get() <= 0){
			lastRunTime = CommonUtil.formatTime(System.currentTimeMillis());
			running = true;
			try {
				return runTask();
			} finally {
				running = false;
			}
		}
		return "stopCounter:" + stopCounter.get() + " > 0,just skip this task";
	}
	
	abstract protected String getSchedule();
	
	abstract protected Object runTask() throws Exception;
	
	final protected void initialSchedule(){
		new Timer(true).schedule(
			new TimerTask(){

				@Override
				public void run() {
					synchronized (this) {					
						String schedule = getSchedule();
						if(!schedule.equals(lastSchedule)) {
							logger.info("get new schedule:{}", schedule);
							if(service != null) {
								service.shutdownNow();
							}
							service = Executors.newScheduledThreadPool(1);							 
							logger.info("reschedule task with:{}", schedule);
							scheduleTask(service, new Runnable(){
								public void run() {	
									try {
										syncRunTask();
									} catch (Exception e) {
										logger.error(e);
									}
								}}, schedule);							 
							lastSchedule = schedule;
						}
					}
				}
			},
			10,
			20*1000
		);
	}
	
	protected void scheduleTask(ScheduledExecutorService service,
			Runnable counteredTask, String schedule) {
		service.scheduleWithFixedDelay(counteredTask, 10, 
				Long.parseLong(schedule), TimeUnit.SECONDS);	
		//timer.schedule(counteredTask, 10, Long.parseLong(schedule) * 1000);
	}	
	
	boolean isEnforce(String info){
		if(info == null || !info.equalsIgnoreCase("enforce")){
			return false;
		}
		return true;
	}

	
	protected String status() {
		return (running ? "running" : "idle") + ":" + stopCounter.get();
	}
	
	public boolean scheduleIsStop() {
		return stopCounter.get() > 0;
	}

	@Override
	public String startSchedule(String info) {
		if(isEnforce(info)){
			stopCounter.set(0);
		}
		else {
			stopCounter.decrementAndGet();
		}
		logger.info("start schedule with status:{}", status());
		return status();
	}


	@Override
	public   String stopSchedule(String info) {
		if(isEnforce(info)){
			stopCounter.set(1);
		} else {
			stopCounter.incrementAndGet();
		}		
		logger.info("stop schedule with status:{}", status());
		return status();
	}
	
	@Override
	public void getMonitorInfos(String prefix, Map<String, Object> map){
		super.getMonitorInfos(prefix, map);
		prefix += AbstractScheduleServer.class.getSimpleName() + ".";
		map.put(prefix + "lastRunTime", lastRunTime);
	}
	
	@Override
	public void stop() throws Exception{
		try {
			super.stop();
		} finally {
			if (service != null) service.shutdownNow();
		}
	}

}
