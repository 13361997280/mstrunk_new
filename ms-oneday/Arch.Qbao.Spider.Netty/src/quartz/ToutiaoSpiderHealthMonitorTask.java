package quartz;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;

import data.spider.ToutiaoApiProcesser;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2013
 * 
 * @Company ctrip.com
 * 
 * @Author hz
 * 
 * @Version 1.0
 * 
 * @Create-at 2014-5-21 11:03:03
 * 
 * @Modification-history <br>
 *                       Date Author Version Description <br>
 *                       ------------------------------------------------------
 *                       ---- <br>
 *                       2013-5-21 11:03:03 hz 1.0 Newly created
 */

public class ToutiaoSpiderHealthMonitorTask implements Job {
	private static ToutiaoSpiderHealthMonitorTask  ToutiaoSpiderHealthMonitorTask;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 爬虫最新监控状态监控map，键值对定义
	 * ++SPIDER启动时间
	 *  key=SPIDER_INIT_TIME , value=new date().tostring();
	 * ++最新活跃线程
	 * key=THREAD_LIVE_NUM_LASTLY  , value = liveNum  + _new date().tostring();
	 * ++最新检查时间
	 * key=HEALTH_CHECK_TIME_LASTLY , value=new date().tostring();
	 * ++最新报错时间
	 * key=FAILURE_TIME_LASTLY , value=failure descrip +_new date().tostring();
	 * ++最新报错原因＋时间 ，主要监控，启动失败， 新活跃线程数，es数据插入失败，
	 * key=FAILURE_DESCPT_LASTLY , value=failure descrip +_new date().tostring();
	 * ++最新重启spider时间
	 *  key=SPIDER_RESTART_TIME_LASTLY , value=new date().tostring();
	 * **/
	private static  LinkedHashMap<String, Object> spiderStatusMap = new LinkedHashMap<>();
	
	private static boolean toutiaoUrlListPageJsonCheck(Page page){
		int newsListNum=JSON.parseObject(page.getRawText()).getJSONArray("data").size();
		
		return true;
	}
	
	
	
	
	
	public final static ToutiaoSpiderHealthMonitorTask getInstance() {//单例模式
		try {
			if (ToutiaoSpiderHealthMonitorTask == null) {
				synchronized (ToutiaoSpiderHealthMonitorTask.class) {
					if (ToutiaoSpiderHealthMonitorTask == null) {
						ToutiaoSpiderHealthMonitorTask = new ToutiaoSpiderHealthMonitorTask();
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return ToutiaoSpiderHealthMonitorTask;
	}
	
	
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// quartz参数传递到这里得到
		//String appId = context.getJobDetail().getJobDataMap().getString("appId");
		ToutiaoSpiderHealthMonitorTask.getInstance().Process();
	}

	private void Process() {
		try {
			Spider spider = ToutiaoApiProcesser.getInstance().getSpider();
			ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().put("THREAD_LIVE_NUM_LASTLY", spider.getThreadAlive()+"___"+sdf.format(new Date()));
			
			/**
			 * 头条新闻不停的抓，所以只要活跃线程数量为0，则doProcess(), 重启spider
			 **/
			if (true) {
				System.out.println("+++toutiaospider have no threads living ,restart spider now,now= "+new Date().toLocaleString());
				ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().put("FAILURE_TIME_LASTLY", sdf.format(new Date()));
				ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().put("FAILURE_DESCPT_LASTLY", "thread live num = 0"+"___"+sdf.format(new Date()));
				
				try {
					ToutiaoApiProcesser.getInstance().restart();
					ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().put("SPIDER_RESTART_TIME_LASTLY", sdf.format(new Date()));
					
				} catch (Exception e) {
					System.out.println("+++toutiaospider have no threads living ,restart spider fail now=  " +new Date().toLocaleString());
					ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().put("FAILURE_DESCPT_LASTLY", "thread live num = 0 and restart failure"+"___"+sdf.format(new Date()));
					e.printStackTrace();
				}

			}else {
				ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().put("HEALTH_CHECK_TIME_LASTLY", sdf.format(new Date()));
				
				System.out.println("+++toutiaospider living threads check good ,now= "+new Date().toLocaleString());
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * @return the spiderStatusMap
	 */
	public static LinkedHashMap<String, Object> getSpiderStatusMap() {
		return spiderStatusMap;
	}





	/**
	 * @param spiderStatusMap the spiderStatusMap to set
	 */
	public static void setSpiderStatusMap(LinkedHashMap<String, Object> spiderStatusMap) {
		ToutiaoSpiderHealthMonitorTask.spiderStatusMap = spiderStatusMap;
	}



	public String SpiderStatusMapPrint(){
		try {
			Set<String> keys = ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().keySet();
			StringBuffer sb = new StringBuffer();
			sb.append("=========SpiderStatesMap info start =================").append("\r\n").append("<br/>").append("<br/>");
			for (String key : keys) {
				sb.append(key+" = "+ToutiaoSpiderHealthMonitorTask.getInstance().getSpiderStatusMap().get(key)).append("\r\n").append("<br/>");
			}
			sb.append("<br/>").append("=========SpiderStatesMap info end =================").append("\r\n").append("<br/>").append("<br/>");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "unknow error 500_ToutiaoSpiderHealthMonitorTask !";
		
	}


	public static void main(String[] args) {
		// EngineDailyTask.doTask();
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		String pct = percentFormat.format(0.9997078);
		System.out.println(pct);
	}

}
