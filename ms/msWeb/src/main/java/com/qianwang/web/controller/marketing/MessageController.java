package com.qianwang.web.controller.marketing;

import com.qianwang.dao.domain.Message;
import com.qianwang.service.marketing.MessageService;
import com.qianwang.service.marketing.ShortMessage;
import com.qianwang.web.auth.CheckEnum;
import com.qianwang.web.auth.MsAuth;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消息模块
 *
 * @author song.j
 * @create 2017 -05-15 15:15:09
 */
@RequestMapping("message")
@Controller
public class MessageController extends BaseController{

    /**
     * The Message service.
     */
    @Autowired
    MessageService messageService;

    @Autowired
    ShortMessage shortMessage;

//    /**
//     * 消息提交 （停用。活动页面上面做了消息提交）
//     *
//     * @param message the marketing
//     * @return the ajax result
//     */
//    @RequestMapping("save")
//    @ResponseBody
//    public AjaxResult save(Message message) {
//        messageService.saveMessage(message);
//        Map resultData = new HashMap<String, Integer>();
//        resultData.put("messageId", message.getId());
//        return AjaxResult.successResult(resultData);
//    }

    /**
     * 发送短信测试
     *
     * @param phone   the phone
     * @param content the content
     * @return the ajax result
     */
    @RequestMapping("sendMobile")
    @ResponseBody
    @MsAuth(role = CheckEnum.CHECK)
    public AjaxResult sendMobile(String phone, String  content) {

        if(StringUtils.isEmpty(content) || content.length() > 300 ){
            return AjaxResult.failureResult("短信内容不能大于300个字");
        }

        shortMessage.sendMessagePhone(phone,content);
        return AjaxResult.success();
    }


    /**
     * 获取h5链接
     * @return
     */
    @RequestMapping("wapLink")
    @ResponseBody
    public AjaxResult getAllWapLink(){

        Integer userId = getCurrentUserId();

        List<Message> list = messageService.getAllWapLink(userId);

        return AjaxResult.successResult(list);
    }
}
