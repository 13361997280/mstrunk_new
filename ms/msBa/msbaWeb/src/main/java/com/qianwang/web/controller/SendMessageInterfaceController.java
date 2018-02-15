package com.qianwang.web.controller;

import com.qianwang.web.result.AjaxResult;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dn0879 on 16/5/16.
 */
@Controller
@RequestMapping("/send_massage/**")
public class SendMessageInterfaceController {

    private final static Logger logger = Logger.getLogger(SendMessageInterfaceController.class);

    @RequestMapping(value = "custom_info")
    @ResponseBody
    public AjaxResult sendMassage(String phoneNumber,String sendInfo){
        try {
            /*if((!"".equals(sendInfo)) && sendInfo !=null ) {
                String newsendInfo= URLEncoder.encode(sendInfo, "utf-8");
                if(!newsendInfo.equals(sendInfo)){
                    logger.info("发送短信内容不符合格式，返回失败");
                    return AjaxResult.successResult("fail");
                }
            }*/
           /* SMSSendApi.sendSmsNotify(44, phoneNumber, sendInfo,
                    new SimpleDateFormat(
                            "yyyyMMddHHmmss").format(Calendar.getInstance()
                            .getTime()), 2);*/
            logger.info( "短信发送成功：" + phoneNumber+":"+ sendInfo );
            return AjaxResult.successResult("success");
        }catch (Exception e){
            logger.error("custom message error!",e);
            return AjaxResult.successResult("fail");
        }
    }
}
