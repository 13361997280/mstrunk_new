package com.qianwang.service.marketing;

import com.qianwang.dao.domain.Message;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.util.http.HttpUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信管理
 *
 * @author song.j
 * @create 2017-05-22 16:16:18
 **/
@Component
public class ShortMessage extends UrlProperties{

    private final static Logger LOG = LoggerFactory.getLogger(ShortMessage.class);

    /**
     * 发送短信
     *
     * @param phone
     * @param content
     */
    public void sendMessagePhone(String phone, String content) {
        try {

            LOG.info("发送短信：phone = {},content = {}", phone, content);

            Map<String,String> headMap = new HashMap<String, String>();
            headMap.put("mobile",phone);
            headMap.put("content",content);
            String response = HttpUtils.get(sendMessageByPhone,headMap);

            LOG.info("短信结果：phone = {},content = {}, response = {}", phone, content, response);
        } catch (Exception e) {
            LOG.error("发送短信异常 phone = {}, content = {}", phone, content, e);
        }
    }

    public void sendMessageUserId(String userId, String content) {
        try {
            LOG.info("发送短信：userId = {},content = {}", userId, content);

            Map<String,String> headMap = new HashMap<String, String>();
            headMap.put("userId",userId);
            headMap.put("content",content);
            String response = HttpUtils.get(sendMessageByUserId,headMap);

            LOG.info("短信结果：userId = {},content = {}, response = {}", userId, content, response);
        } catch (Exception e) {
            LOG.error("发送短信异常 userId = {}, content = {}", userId, content, e);
        }
    }
    
    /**
     * qbaoApp推送
     * @param userId
     * @param content
     */
    public void sendQbaoAppMessageByUserId(String userIds, Message message) {
        try {
            LOG.info("发送qbaoApp短信：msgId = {}, title = {} content = {}, link = {}, postUrl = {}, userIds = {}", message.getId(), 
            		message.getTitle(), message.getContent(), message.getLink(), sendQbaoAppMessageByUserId, userIds);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userIds", userIds));
            params.add(new BasicNameValuePair("title", message.getTitle()));
            params.add(new BasicNameValuePair("content", message.getContent()));
            params.add(new BasicNameValuePair("url", message.getLink()));

            String response = HttpUtils.post(sendQbaoAppMessageByUserId, params);

            LOG.info("qbaoApp短信结果：msgId = {},content = {}, response = {}", message.getId(), message.getContent(), response);
        } catch (Exception e) {
            LOG.error("qbaoApp发送短信异常 msgId = {}, content = {}", message.getId(), message.getContent(), e);
        }
    }


}
