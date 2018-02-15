package com.qianwang.web.controller.marketing;

import com.qianwang.dao.domain.Message;
import com.qianwang.service.marketing.MessageService;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * h5页面入口
 *
 * @author song.j
 * @create 2017 -05-17 17:17:25
 */
@Controller
@RequestMapping("/h5")
public class WapController extends BaseController {

    private final static Logger LOG = LoggerFactory.getLogger(WapController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    LogHandler logHandler;

    @Autowired
    UrlProperties urlProperties;
    /**
     * 生成h5推广链接
     *
     * @return the ajax result
     */
    @RequestMapping("sendLink")
    @ResponseBody
    public AjaxResult sendLink(Integer messageId, String userSelectGroupId) {

        String currentUserId = getCurrentUserId().toString();

        if (StringUtils.isEmpty(userSelectGroupId)){
            return AjaxResult.failureResult("推送人群不能为空");
        }

        StringBuffer sb = new StringBuffer(urlProperties.serverHost);
        sb.append("/h5/");
        sb.append(messageId);
        sb.append("/");
        sb.append(currentUserId);
        sb.append("/");
        sb.append(userSelectGroupId);
        sb.append("/");
        sb.append("content.do");

        messageService.updateMessage(new Message(messageId, sb.toString()));

        Map resultMap = new HashMap();
        resultMap.put("url", sb.toString());

        Message message = new Message();
        message.setId(messageId);
        message.setWapLink(sb.toString());
        messageService.updateMessage(message);
        return AjaxResult.successResult(resultMap);
    }

    /**
     * 精准用户访问
     *
     * @param mid 消息ID
     * @param pid 推广用户ID
     * @param kid 客群id
     * @param u   精确用户id
     * @return content content
     */
    @RequestMapping(value = "/{mid}/{pid}/{kid}/content", method = RequestMethod.GET)
    public String getContent(HttpServletRequest request,@PathVariable String mid, @PathVariable String pid,
                             @PathVariable
            String kid,
                             @RequestParam(required = false) String u) {
        try {
            Message message = messageService.getMessageById(mid);
            //如果是精确用户访问的话 获取用户的推送时间
            if (u != null) {
                Message shortMessage = messageService.getByMessageId(mid);
                if (shortMessage!=null)
                    message.setSendTime(shortMessage.getSendTime());
            }
            LOG.info("h5访问记录日志 mid = {}, pid = {}, kid = {}", message, pid, kid);
            logHandler.sendMessageLogToEs(message, u,request);
        } catch (Exception e) {
            LOG.error("send log error message = {}", e);
        }
        return "/build/h5/0/index.html?mid=" + mid;
    }


    /**
     * 获取h5内容
     *
     * @param mid 消息ID
     * @return content content
     */
    @RequestMapping(value = "/getShortContent", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getShortContent(String mid) {

        Map resultMap = null;
        try {
            Message message = messageService.getMessageById(mid);

            resultMap = new HashMap();
            resultMap.put("logo", message.getLogo());
            resultMap.put("title", message.getTitle());
            resultMap.put("link", message.getLink());
            resultMap.put("content", message.getContent());
            resultMap.put("shortContent", message.getShortContent());

        } catch (Exception e) {
            LOG.error("getMesShortContent error mid = {} message = {}", mid, e);
        }
        return AjaxResult.successResult(resultMap);
    }


}
