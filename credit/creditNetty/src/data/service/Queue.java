package data.service;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import java.util.Map;

public class Queue {
    private static ESLogger logger = Loggers.getLogger(Queue.class);

    public static void main(String[] args) throws InterruptedException {
    }

    /**
     * 生产
     */
    public static void offer(Map<String,Object> param) {
        CreditMysqlDataService.getInstance().getExecutorService().submit(new Runnable(){
            @Override
            public void run() {
                logger.info("线程号为:" + Thread.currentThread() + "--进队列开始消费,参数列表为:" + param.toString());
                try {
                    String returnStr = CreditMysqlDataService.getInstance().save(param);
                    logger.info("消费结果返回" + returnStr);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error(e);
                }
            }

        });
    }

    public static void logOffer(Map<String,Object> param) {
        CreditDataService.getInstance().getExecutorService().submit(new Runnable(){
            @Override
            public void run() {
                logger.info("中央日志线程号为:" + Thread.currentThread() + "--进中央日志系统队列开始消费,参数列表为:" + param.toString());
                try {
                    CreditDataService.getInstance().logSave(param);
                } catch (Exception e) {
                    logger.error("Queue->LogPoll error:", e.getMessage());
                    // TODO Auto-generated catch block
                }
            }
        });
    }
}