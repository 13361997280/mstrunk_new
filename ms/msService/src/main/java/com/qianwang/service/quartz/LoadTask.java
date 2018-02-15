package com.qianwang.service.quartz;

import com.qianwang.dao.domain.QuartTask;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

/**
 * 任务加载
 *
 * @author song.j
 * @create 2017-05-23 10:10:41
 **/
public class LoadTask {

    private final static Logger LOG = LoggerFactory.getLogger(LoadTask.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private TaskService taskService;

    /**
     * 项目启动时会加载任务
     * 防止服务器启动任务丢失
     *
     * @throws SchedulerException
     */
    public void initTask() throws SchedulerException {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        // 可执行的任务列表
        List<QuartTask> taskList = taskService.findTask();
        LOG.info("任务数 :{}", taskList.size());
        for (QuartTask task : taskList) {
            // 任务名称和任务组设置规则：
            // 名称：task_1 ..
            // 组 ：group_1 ..
            TriggerKey triggerKey = TriggerKey.triggerKey(
                    "task_" + task.getId(), "group_" + task.getId());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 不存在，创建一个
            if (null == trigger) {
                LOG.info("task_{} group_{} 不存在", task.getId(), task.getId());

                JobDetail jobDetail = JobBuilder.newJob(QuartzTaskJob.class)
                        .withIdentity("task_" + task.getId(), "group_" + task.getId()).build();
                jobDetail.getJobDataMap().put("scheduleJob", task);
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(task.getCronExpression());
                // 按新的表达式构建一个新的trigger
                trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity("task_" + task.getId(),
                                "group_" + task.getId())
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                LOG.info("添加任务：{}", task.getCronExpression());
            } else {
                LOG.info("任务task_{} group_{} 已存在", task.getId(), task.getId());
                // trigger已存在，则更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(task.getCronExpression());
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
    }

    /**
     * 添加新的任务
     */
    public void addTask(QuartTask task) throws SchedulerException {

        LOG.info("添加任务：tasK_{} group_{} , 任务时间:{}",task.getId(),task.getId(), task.getCornTime());

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobDetail jobDetail = JobBuilder.newJob(QuartzTaskJob.class)
                .withIdentity("task_" + task.getId(), "group_" + task.getId()).build();
        jobDetail.getJobDataMap().put("scheduleJob", task);
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule(task.getCronExpression());
        // 按新的表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("task_" + task.getId(),
                        "group_" + task.getId())
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
