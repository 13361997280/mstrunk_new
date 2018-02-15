package com.qbao.creditmq;

import com.qbao.creditmq.config.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {
    static  int i =0;

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println(DateUtil.now()+"Received <" + message + ">  ");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}