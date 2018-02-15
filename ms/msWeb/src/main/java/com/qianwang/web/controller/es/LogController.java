package com.qianwang.web.controller.es;

import com.alibaba.fastjson.JSON;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.util.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author song.j
 * @create 2017-08-07 16:16:44
 **/
@Controller
@RequestMapping("/log")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    UrlProperties urlProperties;


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object getSearchLog(String startTime, String endTime, String userId, String page, String size) {
        String url = urlProperties.esAddr + "/getLog?";
        System.out.println(111);
        StringBuffer param = new StringBuffer();
        if (startTime != null) {
            param.append("startTime=");
            param.append(startTime);
            param.append("&");
        }
        if (page != null) {
            param.append("endTime=");
            param.append(endTime);
            param.append("&");
        }
        if (page != null) {
            param.append("userId=");
            param.append(userId);
            param.append("&");
        }
        if (page != null) {
            param.append("page=");
            param.append(page);
            param.append("&");
        }
        if (page != null) {
            param.append("size=");
            param.append(size);
        }

        url += param.toString();

        try {
            String response = HttpUtils.get(url);
            return JSON.parse(response);
        } catch (Exception e) {
            log.error("search es log error ", e);
        }

        return null;
    }
}
