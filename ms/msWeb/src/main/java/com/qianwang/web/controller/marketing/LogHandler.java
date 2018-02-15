package com.qianwang.web.controller.marketing;

import com.qianwang.dao.domain.Message;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.util.http.HttpUtils;
import com.qianwang.util.lang.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * H5访问日志处理器
 *
 * @author song.j
 * @create 2017-05-23 14:14:56
 **/
@Component
public class LogHandler {

    @Autowired
    UrlProperties urlProperties;

    /**
     * 发送h5访问日志
     *
     * @param message
     * @param eUserId
     * @throws Exception
     */
    public void sendMessageLogToEs(Message message, String eUserId, HttpServletRequest request) throws Exception {
        StringBuffer url = new StringBuffer(urlProperties.accessRecordUrl);

        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("sendType", "H5");
        headMap.put("browseDateTime",DateUtil.formatDate(new Date(), DateUtil.TIMESTAMP_PATTERN));

        if (eUserId != null)
            headMap.put("userId", eUserId);

        if (message.getUserSelectGroupId() != null)
            headMap.put("groupId", message.getUserSelectGroupId());

        if (message.getId() != null)
            headMap.put("msgId", message.getId().toString());

        if (message.getSendTime() != null)
            headMap.put("doTaskDateTime", DateUtil.formatDate(message.getSendTime(), DateUtil.TIMESTAMP_PATTERN));

        //访问地址
        if (StringUtils.isNotEmpty(request.getRemoteAddr())){
            headMap.put("browseIp",request.getRemoteAddr());
        }

        //访问设备
        if (StringUtils.isNotEmpty(request.getHeader("User-Agent"))){
            headMap.put("userAgent",request.getHeader("User-Agent"));
        }

        HttpUtils.get(url.toString(), headMap);
    }
}
