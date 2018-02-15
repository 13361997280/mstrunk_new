package com.qbao.search.engine.qbaolog;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.service.EsDataService;
import redis.RedisConst;
import redis.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author song.j
 * @create 2017-07-19 17:17:19
 **/
public class QbaoLogJob {

    private static ESLogger logger = Loggers.getLogger(QbaoLogJob.class);

    private int consumeNum = 500;

    private RedisUtil redisUtil = new RedisUtil();


    public QbaoLogJob() {

    }

    /**
     * 10分钟之后执行。每过10分钟执行一次
     */
    public void start() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logger.info("log job start");
                try {
                    consumeLog();
                } catch (Exception e) {
                    logger.error("log job error", e);
                }
                logger.info("log job end");
            }
        }, 10 * 60 * 1000, 10 * 60 * 1000);
    }


    private void consumeLog() {

        String key = RedisConst.TEGNRONG_ONEDAY_QBAOLOG_QUEUE.key;

        Long lenght = redisUtil.getlen(key);

        logger.info("log queue lenght = {}", lenght);
        for (int i = consumeNum; i < lenght; i += consumeNum) {
            List<Object> result = redisUtil.lPipelinePop(RedisConst.TEGNRONG_ONEDAY_QBAOLOG_QUEUE.key, consumeNum);
            //删除null元素
            logger.info("log queue list size = {}", result.size());
            result.removeAll(Collections.singleton(null));
            logger.info("log queue list remove null size = {}", result.size());
            logger.info("log consume = {} , time-i = {}", result.size(), i);
            try {
                EsDataService.getInstance().saveQbaoLog(result);
            } catch (Exception e) {
                logger.error("log save qbaolog error ", e);
            }
        }
    }

}
