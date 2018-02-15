package com.qbao.creditmq.controller;


import com.qbao.creditmq.config.util.RedisUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController

@RequestMapping("/redis/**")
public class RedisTestController {
    private static final Log log = LogFactory.getLog(RedisTestController.class);

    @Autowired
    RedisUtil redisUtil;






    @RequestMapping("/test")
    public Object signConfigInfo(String userId){
        redisUtil.set("123", "测试");
        System.out.println("进入了方法");
        log.info("sdf");
        return redisUtil.get("123");
    }
}
