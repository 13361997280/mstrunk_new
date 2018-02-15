package com.qianwang.service.job;

import com.qianwang.service.util.DateUtil;
import com.qianwang.util.lang.HttpUtils;
import com.qianwang.util.sms.EmailUtil;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据同步定时器
 *
 * @author song.j
 * @create 2017-11-22 16:16:25
 **/
public class MysqlToEsDataJob {

    protected static final Logger LOG = LoggerFactory.getLogger(MysqlToEsDataJob.class);


    @Value("${credit.netty.host}")
    String creditUrl;

    @Autowired
    EmailUtil emailUtil;

    public void execute() throws JobExecutionException {

        String url = creditUrl + "/command";

        Map param = new HashMap<>();

        param.put("type", "indexupdate");
        param.put("index", "busscore");
        param.put("table", "active_user_credit_sync_final_d");
        param.put("key", "user_id");
        param.put("dt", "stat_date");
        param.put("date", DateUtil.getNextDay(new Date(), -1));

        String message = null;

        try {
            message= HttpUtils.get(url, param);
        } catch (Exception e) {
            LOG.error("MysqlToEsDataJob-execute error ", e);
            message = e.getMessage();
        } finally {
            emailUtil.sendEmail(message,true);
        }

    }
}
