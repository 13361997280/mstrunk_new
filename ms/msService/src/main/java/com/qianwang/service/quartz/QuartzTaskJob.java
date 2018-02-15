package com.qianwang.service.quartz;

import com.qianwang.dao.domain.ActSend;
import com.qianwang.dao.domain.QuartTask;
import com.qianwang.mapper.ActSendMapper;
import com.qianwang.mapper.QuartTaskMapper;
import com.qianwang.service.marketing.MessageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务执行job
 *
 * @author song.j
 * @create 2017-05-23 10:10:42
 **/
public class QuartzTaskJob implements Job {

    private final static Logger LOG = LoggerFactory.getLogger(QuartzTaskJob.class);

    @Autowired
    MessageService messageService;
    @Autowired
    ActSendMapper actSendMapper;
    @Autowired
    QuartTaskMapper quartTaskMapper;

    @Transactional
    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        try {
            LOG.info("任务运行开始");
            QuartTask task = (QuartTask) context.getMergedJobDataMap().get("scheduleJob");
            messageService.sendMessage(task.getUserSelectId(),task.getMessageId());
            task.setEnable(false);
            quartTaskMapper.updateByPrimaryKeySelective(task);

            ActSend actSend = new ActSend();
            actSend.setId(task.getActivityId());
            actSend.setStatus((byte) 1);
            actSendMapper.updateByPrimaryKeySelective(actSend);
            LOG.info("任务运行名称: [" + task.getTaskName() + "]");
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
