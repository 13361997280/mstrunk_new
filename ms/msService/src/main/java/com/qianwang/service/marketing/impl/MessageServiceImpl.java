package com.qianwang.service.marketing.impl;

import com.qianwang.dao.domain.Message;
import com.qianwang.mapper.MessageMapper;
import com.qianwang.service.marketing.MessageService;
import com.qianwang.service.marketing.ShortMessage;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.util.http.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Message service.
 *
 * @author song.j
 * @create 2017 -05-16 10:10:15
 */
@Service
public class MessageServiceImpl extends UrlProperties implements MessageService {

    private final static Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**
     * The Message mapper.
     */
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    ShortMessage shortMessage;

    @Override
    public int saveMessage(Message message) {
        messageMapper.insertSelective(message);
        return message.getId();
    }

    @Override
    public Message getMessageById(String messageId) throws Exception {
        return messageMapper.selectByPrimaryKey(Integer.valueOf(messageId));
    }

    public Message getByMessageId(String messageId){
        return messageMapper.selectByMessageId(messageId);
    }

    @Override
    public String getMesShortContent(String messageId) throws Exception {
        return getMessageById(messageId).getShortContent();
    }

    @Override
    public int hasShortContent(String url) {
        return 0;
    }

    @Override
    public int updateMessage(Message message) {
        message.setUpdateTime(new Date());
        return messageMapper.updateByPrimaryKeySelective(message);
    }

    @Override
    public List<Message> getAllWapLink(Integer userId) {
        return messageMapper.selectLink(userId);
    }

    /**
     * 根据收藏ID查去ES查询出用户ID进行发送短信
     * 
     * 增加qbaoApp推送通道消息发送
     *
     * @param selectId      收藏ID
     * @param messageId     消息ID
     */
	public void sendMessage(final String selectId, final int messageId) {

		final Message message = messageMapper.selectByPrimaryKey(messageId);
		if (message.getType() == 3) {
			// qbaoApp推送
			System.out.println("qbaoAPP推送开始" + message.getContent()+ message.getTitle());
			try {
				boolean flag = true;
				int page = 0;
				while (flag) {
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("id", selectId);
					paraMap.put("page", page);

					String userIds = HttpUtils.get(getUserIdsUrl, paraMap);

					LOG.info("获取userIds url = {},page = {}, 客群ID = {} 结果 = {}", getUserIdsUrl, page, selectId, userIds);

					if (StringUtils.isNotEmpty(userIds)) {
						page++;
						flag = true;
						// 调用qbaoApp推送接口发送消息
		                shortMessage.sendQbaoAppMessageByUserId(userIds, message);
					} else {
						flag = false;
					}					
				}

			} catch (Exception e) {
				LOG.error("sendMessage 发送短信失败：message = {}", e.getMessage());
			}
		} else {
			try {
				// 短信通道推送线程
				boolean flag = true;
				int page = 0;
				while (flag) {
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("id", selectId);
					paraMap.put("page", page);

					String userIds = HttpUtils.get(getUserIdsUrl, paraMap);

					LOG.info("获取userIds url = {},page = {}, 客群ID = {} 结果 = {}", getUserIdsUrl, page, selectId, userIds);

					if (StringUtils.isNotEmpty(userIds)) {
						page++;
						flag = true;
					} else {
						flag = false;
					}
					// new SendMessageHandler(userIds.split(","),
					// message).run();
					threadSendMessage(userIds.split(","), message);
				}

			} catch (Exception e) {
				LOG.error("sendMessage 发送短信失败：message = {}", e.getMessage());
			}

		}
	}

    private void threadSendMessage(String[] eUserIds,Message message) {

        if (eUserIds == null || eUserIds.length < 1) {
            LOG.info("SendMessageHandler.run eUserIds is null");
            return;
        }
        if (message == null) {
            LOG.info("SendMessageHandler.run message is null");
            return;
        }

        String content = message.getContent();

        String wapLink = null;
        if (message.getMessageId() != null) {
            wapLink = messageMapper.selectByPrimaryKey(message.getMessageId()).getWapLink();
        }

        boolean flag = false;
        if (wapLink != null && content.indexOf(wapLink) > 0) {
            flag = true;
        }

        for (int i = 0; i < eUserIds.length; i++) {
            if (StringUtils.isNotEmpty(eUserIds[i])) {
                String sendContent = content;
                if (flag) {
                    sendContent = sendContent.replace(wapLink, wapLink + "?u=" + eUserIds[i]);
                }
                shortMessage.sendMessageUserId(eUserIds[i], sendContent);
            }
        }
    }

//    class SendMessageHandler implements Runnable{
//
//        String[] eUserIds;
//
//        Message message;
//
//        public SendMessageHandler(String[] eUserIds,Message message) {
//            this.eUserIds = eUserIds;
//            this.message = message;
//        }
//
//        @Override
//        public void run() {
//            Thread current = Thread.currentThread();
//            LOG.info("message thread info id = {},name = {}", current.getId(), current.getName());
//
//            if (eUserIds == null || eUserIds.length<1){
//                LOG.info("SendMessageHandler.run eUserIds is null");
//                return;
//            }
//            if (message == null){
//                LOG.info("SendMessageHandler.run message is null");
//                return;
//            }
//
//            String content = message.getContent();
//
//            String wapLink = null;
//            if (message.getMessageId()!=null){
//                wapLink = messageMapper.selectByPrimaryKey(message.getMessageId()).getWapLink();
//            }
//
//            boolean flag = false;
//            if (wapLink != null && content.indexOf(wapLink) > 0) {
//                flag = true;
//            }
//
//            for (int i = 0; i < eUserIds.length; i++) {
//                if (StringUtils.isNotEmpty(eUserIds[i])){
//                    String sendContent = content;
//                    if (flag){
//                        sendContent = sendContent.replace(wapLink, wapLink + "?u=" + eUserIds[i]);
//                    }
//                    shortMessage.sendMessageUserId(eUserIds[i],sendContent);
//                }
//            }
//        }
//    }
}
