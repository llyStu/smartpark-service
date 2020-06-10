package com.vibe.mapper.information;

import com.vibe.pojo.infomation.Message;
import com.vibe.pojo.infomation.MessageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao {

	void addMessage(Message message);

	List<Message> getSearchMessages(MessageVo vo);

	Message getDetail(int mid);

	void deleteMessage(@Param("ids") int[] ids);
}
