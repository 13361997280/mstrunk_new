package com.qianwang.web.controller;

import com.alibaba.fastjson.JSON;
import com.qianwang.service.redis.RedisConst;
import com.qianwang.service.redis.RedisUtil;
import com.qianwang.util.http.HttpUtils;
import com.qianwang.web.result.AjaxResult;
import com.qianwang.web.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author song.j
 * @create 2017-05-25 18:18:19
 **/
@RequestMapping("/wechat")
@Controller
public class WeChatController {

//    https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxbd7f2ffc04fba2ed&secret=e089cab82d3d9656e6a0abbeca726d96

    @Autowired
    RedisUtil redisUtil;

    private static final String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";
    private static final String ticket_url = "https://api.weixin.qq" +
            ".com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    private static final String grant_type = "client_credential";
    private static final String appid = "wxbd7f2ffc04fba2ed";
    private static final String secret = "e089cab82d3d9656e6a0abbeca726d96";

    @RequestMapping(value = "token", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getToken(@RequestParam String url) {

        try {

            String accessToken = getToken();
            if (accessToken == null)
                return AjaxResult.failureResult("获取token失败");

            String ticket = getTicket(accessToken);
            if (ticket == null)
                return AjaxResult.failureResult("获取ticket失败");

            String timestamp=getTime();

            String noncestr = "73cdb98f91e7d7a7f574";
            String signature = SignUtil.getJsSdkSign(noncestr, ticket, timestamp, url);

            Map reidsMap =new HashMap();
            reidsMap.put("timestamp",timestamp);
            reidsMap.put("signature",signature);


            Map resultMap = new HashMap();
            resultMap.put("appid", appid);
            resultMap.put("ticket", ticket);
            resultMap.put("timestamp", timestamp);
            resultMap.put("noncestr", noncestr);
            resultMap.put("signature", signature);
            resultMap.put("url", url);

            return AjaxResult.successResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.failureResult("获取失败");
    }

    private String getTime(){
        String timestamp = null;
        timestamp = redisUtil.get(RedisConst.MS_WECHAT_TIMESTAMP.key);
        if (timestamp == null){
            timestamp = String.valueOf(new Date().getTime());
            timestamp = timestamp.substring(0, timestamp.length() - 3);

            redisUtil.set(RedisConst.MS_WECHAT_TIMESTAMP.key, timestamp, RedisConst.MS_WECHAT_TIMESTAMP.expired);
        }
        return timestamp;
    }


    private String getTicket(String accessToken) {

        String ticket = null;
        String redisTicket = redisUtil.get(RedisConst.MS_WECHAT_JSAPI_TICKET.key);
        if (redisTicket != null)
            return redisTicket;


        try {
            String ticketResp = HttpUtils.get(String.format(ticket_url, accessToken));

            if (ticketResp == null)
                return null;

            Map<String, String> ticketMap = JSON.parseObject(ticketResp, Map.class);
            if (ticketMap == null)
                return null;

            ticket = ticketMap.get("ticket");
            redisUtil.set(RedisConst.MS_WECHAT_JSAPI_TICKET.key, ticket, RedisConst.MS_WECHAT_JSAPI_TICKET.expired);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ticket;
    }

    private String getToken() {

        String accessToken = null;
        String redisAccessToken = redisUtil.get(RedisConst.MS_WECHAT_ACCESS_TOKEN.key);
        if (redisAccessToken != null)
            return redisAccessToken;

        try {
            String response = HttpUtils.get(String.format(token_url, grant_type, appid, secret));

            Map<String, String> tokenMap = JSON.parseObject(response, Map.class);

            accessToken = tokenMap.get("access_token");
            redisUtil.set(RedisConst.MS_WECHAT_ACCESS_TOKEN.key, accessToken, RedisConst.MS_WECHAT_ACCESS_TOKEN.expired);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

}
