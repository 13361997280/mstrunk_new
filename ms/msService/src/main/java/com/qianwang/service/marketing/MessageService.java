package com.qianwang.service.marketing;

import com.qianwang.dao.domain.Message;

import java.util.List;

/**
 * @author song.j
 * @create 2017-05-16 10:10:15
 **/
public interface MessageService {

    Message getByMessageId(String messageId);

    int saveMessage(Message message);

    Message getMessageById(String messageId) throws Exception;

    String getMesShortContent(String messageId) throws Exception;

    int hasShortContent(String url);

    int updateMessage(Message message);

    List<Message> getAllWapLink(Integer userId);

    void sendMessage(String selectId,int messageId);

}
