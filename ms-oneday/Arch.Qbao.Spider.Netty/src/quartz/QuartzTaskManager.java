package quartz;

import java.util.Calendar;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

/**
 * can run timer task reduce to minut/per reduce to hour/per reduce to day/per
 * ruduce to month/per ruduce to year/per if loss some log period , can init
 * special customized task to add in logs;
 * // 复杂调度
	// 格式: [秒] [分] [小时] [日] [月] [周] [年]
	// mmContrigger.setCronExpression("30 * * * * ? ");//每分钟 30秒启动
	// 每分钟, 0-5秒，10-15秒启动
	// mmContrigger.setCronExpression("0-5,10-15 * * * * ? ");
	// mmContrigger.setCronExpression("0 30 * * * ? ");//每小时30分启动
	// mmContrigger.setCronExpression("0 0 10 * * ? ");//每天10点启动
	// mmContrigger.setCronExpression("0 0 0 15 * ? ");//每月启动
	// mmContrigger.setCronExpression("0 0 0 1 6 ? * ");//每年启动
 **/
public class QuartzTaskManager {
	private static final ESLogger logger = Loggers.getLogger(QuartzTaskManager.class);
	// 通过schedulerFactory获取一个调度器
	SchedulerFactory schedulerfactory = new StdSchedulerFactory();
	public static long adjustDate = Config.get().getInt("log.task.hbase.reduce.adjustDate", 60 * 60 * 2 * 1000);
	private static QuartzTaskManager quartzTaskManager;

	public static final QuartzTaskManager getInstance() {
		if (quartzTaskManager == null) {
			synchronized (QuartzTaskManager.class) {
				if (quartzTaskManager == null) {
					quartzTaskManager = new QuartzTaskManager();
				}
			}
		}
		return quartzTaskManager;
	}

	public QuartzTaskManager() {
		
		//每30秒
		//TaskStart( "ToutiaoSpiderHealthMonitorTask",  "59 * * * * ?", ToutiaoSpiderHealthMonitorTask.class);
		
		//每63秒测试关闭头条spider，上线前注销任务
	    //TaskStart( "ToutiaoSpiderStopTask",  "53 * * * * ?", ToutiaoSpiderStopTask.class);
				
		
		
		
		// taskStart("RecommendDicTask", "0 1 4 * * ? ",
		// RecommendAllDicTask.class);
		// taskStart("UbtTask", "0 40 2 * * ? ", UbtTask.class);
		// taskStart("PropertySolrStaticTask", "0 30 6 * * ? ",
		// PropertySolrStaticTask.class);
		// taskStart("RecommendDicTask", "0 1 4 * * ? ", RecommendDicTask.class);
		// taskStart("RecommendPinYin2dyCnDic_subTask", "0 3 4 * * ? ",
		// RecommendPinYin2dyCnDic_subTask.class);

		// HbaseTask("920601", HbaseLogReducer.DATE_LEVEL_HOUR,
		// "swd_has_return_group_search", "search_logs_analysis",
		// "30 01 * * * ? ", adjustDate);

		// Hive task
		// HiveTask("topkeywordsearchnumber", "0 25 10 * * ? ",
		// TopKeywordSearchNumberService.class);

		// EngineDaily
		// EngineDailyTask("920601", "0 23 8 * * ? ",
		// com.ctrip.search.lexis.engine.daily.report.EngineDailyTask.class);

		// engineDaily Email
		// EngineDailyTask("engineDailyMail", "0 5 8 * * ? ",
		// com.ctrip.search.lexis.engine.daily.report.EngineDailyMailTask.class);
		// EngineDailyTask("engineDailyMail", "30 01 13 * * ?
		// ",com.ctrip.search.lexis.engine.daily.report.EngineDailyMailTask.class);

	}

	
	
	//不带传递启动参数的任务初始化
	public void TaskStart(String taskName, String TimerRegex, Class clz) {
		try {
			Scheduler hhSchedul = schedulerfactory.getScheduler();
			JobDetail hhcondJob = new JobDetail("name." + taskName, "group." + taskName, clz);
			CronTrigger hhContrigger = new CronTrigger("name." + taskName, "group." + taskName);
			hhContrigger.setCronExpression(TimerRegex);// 定时规则
			hhSchedul.scheduleJob(hhcondJob, hhContrigger);
			hhSchedul.start();
			logger.info("+++++quartz." + taskName + " 任务启动 " + Calendar.getInstance().getTime() + "__" + taskName);

		} catch (Exception e) {

		}

	}

	//带传递启动参数的任务初始化
	public void taskStart(String name,String group, Map<String, String> kvMap,String TimerRegex, Class clz) {
		try {
			Scheduler hhSchedul = schedulerfactory.getScheduler();
			JobDetail hhcondJob = new JobDetail(name, group, clz);
			// 传参
			hhcondJob.getJobDataMap().putAll(kvMap);
			CronTrigger hhContrigger = new CronTrigger(name, group);
			hhContrigger.setCronExpression(TimerRegex);// 定时规则
			hhSchedul.scheduleJob(hhcondJob, hhContrigger);
			hhSchedul.start();
			logger.info("+++quartzTaskMng任务启动 " + Calendar.getInstance().getTime() + "__" + name+"_"+group);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

	}
}
