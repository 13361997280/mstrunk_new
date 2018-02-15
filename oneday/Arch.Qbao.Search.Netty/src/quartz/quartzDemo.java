package quartz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * CronTrigger任务调度器
 * 
 * @author lcy Dec 1, 2011
 */
public class quartzDemo {
	private static SchedulerFactory sf = new StdSchedulerFactory();

	/**
	 * 根据任务名和任务组名查询一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @return
	 * @throws SchedulerException
	 */
	public static JobDetail getJob(String jobName, String jobGroupName) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = sched.getJobDetail(jobName, jobGroupName);
		if (jobDetail != null) {
			return jobDetail;
		}
		return null;
	}

	/**
	 * 添加一个CronTrigger定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param job
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @param params
	 *            传递参数
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String time,
			HashMap<String, Object> params, Job jobClass) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass.getClass());// 任务名，任务组，任务执行类
		if (params != null) {
			jobDetail.getJobDataMap().put("params", params);
		}
		// 触发器
		CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
		trigger.setCronExpression(time);// 触发器时间设定
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 添加一个SimpleTrigger定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param startTime
	 *            开始时间
	 * @param repeatInterval
	 *            间隔时间
	 * @param repeatCount
	 *            调用次数
	 * @param jobClass
	 *            任务
	 * @param params
	 *            传递参数
	 * @throws SchedulerException
	 */
	public static void addJob(String jobName, String jobGroup, String triggerName, String triggerGroup, Date startTime, Integer repeatInterval,
			Integer repeatCount, Job jobClass, HashMap<String, Object> params) throws SchedulerException {
		JobDetail jobDetail = new JobDetail(jobName, jobGroup, jobClass.getClass());
		if (params != null) {
			jobDetail.getJobDataMap().put("params", params);
		}
		// 触发器
		SimpleTrigger simpleTrigger = new SimpleTrigger(triggerName, triggerGroup);
		// 设置开始时间
		simpleTrigger.setStartTime(startTime);
		// 设置间隔时间
		simpleTrigger.setRepeatInterval(repeatInterval);
		// 设置调用次数
		simpleTrigger.setRepeatCount(repeatCount);

		Scheduler sched = sf.getScheduler();
		sched.scheduleJob(jobDetail, simpleTrigger);
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 根据任务名和任务组名修改任务的触发时间
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyJobTime(String jobName, String jobGroupName, String time) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		Trigger[] trigger = sched.getTriggersOfJob(jobName, jobGroupName);
		if (trigger != null) {
			for (int i = 0; i < trigger.length; i++) {
				((CronTrigger) trigger[i]).setCronExpression(time);
				sched.resumeTrigger(trigger[i].getName(), trigger[i].getGroup());
			}
		}
	}

	/**
	 * 根据触发器名修改一个任务的触发时间
	 * 
	 * @param triggerName触发器名
	 * @param triggerGroupName触发器组名
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyTriggerTime(String triggerName, String triggerGroupName, String time) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		Trigger trigger = sched.getTrigger(triggerName, triggerGroupName);
		if (trigger != null) {
			// 修改时间
			((CronTrigger) trigger).setCronExpression(time);
			// 重启触发器
			sched.resumeTrigger(triggerName, triggerGroupName);
		}
	}

	/**
	 * 移除任务
	 * 
	 * @param jobName任务名
	 * @param jobGroupName任务组名
	 * @param triggerGroupName触发器组名
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerGroupName) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		String[] triggerNames = sched.getTriggerNames(triggerGroupName);
		if (triggerNames != null) {
			for (int i = 0; i < triggerNames.length; i++) {
				sched.pauseTrigger(triggerNames[i], triggerGroupName);// 停止触发器
				sched.unscheduleJob(triggerNames[i], triggerGroupName);// 移除触发器
			}
		}
		sched.deleteJob(jobName, jobGroupName);// 删除任务
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName任务名
	 * @param jobGroupName任务组名
	 * @param triggerName触发器名
	 * @param triggerGroupName触发器组名
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
		sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
		sched.deleteJob(jobName, jobGroupName);// 删除任务
	}

	/**
	 * 停止一个任务 需要注意的是：这里停止的job必须是实现InterruptableJob的
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @throws SchedulerException
	 */
	public static void stopJob(String jobName, String jobGroupName) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		sched.interrupt(jobName, jobGroupName);
	}

	public static void main(String[] args) {
	}

}