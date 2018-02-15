package quartz;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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

public class EngineDailyMailTaskDemo implements Job {
	public String appId;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public EngineDailyMailTaskDemo() {
		super();
	}

	public EngineDailyMailTaskDemo(String appId) {
		super();
		this.appId = appId;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//quartz参数传递到这里得到
		String appId = context.getJobDetail().getJobDataMap().getString("appId");
		this.appId = appId;
		Process();
	}

	private void Process() {
		try {} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		// EngineDailyTask.doTask();
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		String pct = percentFormat.format(0.9997078);
		System.out.println(pct);
	}

}
