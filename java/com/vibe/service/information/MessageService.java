package com.vibe.service.information;

import com.vibe.pojo.infomation.Message;
import com.vibe.pojo.infomation.MessageVo;
import com.vibe.utils.Page;

public interface MessageService {

    void sendMessage(Message message);

    Page<Message> getSearchMessages(MessageVo vo, int pageNum, int pageSize);

    Message getDetail(int mid);

    void deleteMessage(int[] id);

}
