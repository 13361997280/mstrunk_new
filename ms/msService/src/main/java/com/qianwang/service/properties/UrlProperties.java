package com.qianwang.service.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 配置文件加载
 *
 * @author song.j
 * @create 2017-05-25 11:11:47
 **/
@Component
public class UrlProperties {

    @Value("${sendMessageByUserId}")
    public String sendMessageByUserId;

    @Value("${sendMessageByPhone}")
    public String sendMessageByPhone;

    @Value("${es.addr}")
    public String esAddr;

    @Value("${getUserIdsUrl}")
    public String getUserIdsUrl;

    @Value("${accessLogRecordUrl}")
    public String accessRecordUrl;

    @Value("${serverHost}")
    public String serverHost;

    @Value("${getActiveUserCount}")
    public String getActiveUserCount;

    @Value("${getIndexActivityInfo}")
    public String getIndexActivityInfo;

    @Value("${redisExpiredTime}")
    public String redisExpiredTime;
    
    @Value("${sendQbaoAppMessageByUserId}")
    public String sendQbaoAppMessageByUserId;

    @Value("#{'${messageUsers}'.split(',')}")
    public List<Integer> messgaeUsers;
}
