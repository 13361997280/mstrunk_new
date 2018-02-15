package com.qianwang.service.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.qianwang.util.lang.HttpUtils;

/**
 * es数据导入定时任务
 */
public class EsDataInputJob {

    private final static Logger LOG = LoggerFactory.getLogger(EsDataInputJob.class);

    @Value("${lbs_input_url}")
    private String lbsDataInputUrl;
    
    @Value("${toutiao_input_url}")
    private String toutiaoDataInputUrl;
    
    @Value("${qbaolog_input_url}")
    private String qbaoLogDataInputUrl;
    
    public void lbsDataInput() {
        LOG.info("lbsDataInput-job start");
        try {
        	System.out.println("lbs定时任务触发：url=" + lbsDataInputUrl);
			HttpUtils.get(lbsDataInputUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        LOG.error("lbsDataInput-job error" + e.getMessage());
		}
    }
    
    public void touTiaoDataInput() {
    	LOG.info("touTiaoDataInput-job start");
        try {
        	System.out.println("toutiao定时任务触发：url=" + toutiaoDataInputUrl);
			HttpUtils.get(toutiaoDataInputUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        LOG.error("touTiaoDataInput-job error" + e.getMessage());
		}
    }
    
    public void qbaoLogDataInput() {
    	LOG.info("qbaoLogDataInput-job start");
        try {
        	System.out.println("qbaolog定时任务触发：url=" + qbaoLogDataInputUrl);
			HttpUtils.get(qbaoLogDataInputUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        LOG.error("qbaoLogDataInput-job error" + e.getMessage());
		}
    }
}
