package com.vibe.service.information;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.information.MessageDao;
import com.vibe.pojo.infomation.Message;
import com.vibe.pojo.infomation.MessageVo;
import com.vibe.utils.Page;

@Repository
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageDao msgDao;
	
	@Override
	public void sendMessage(Message message) {
		message.setSendtime(new Date(System.currentTimeMillis()));
		message.setState(1);
		msgDao.addMessage(message);
	}

	@Override
	public Page<Message> getSearchMessages(MessageVo vo, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Message> list = msgDao.getSearchMessages(vo);
		PageInfo<Message> page = new PageInfo<>(list);
		
		Page<Message> result = new Page<>();
		result.setRows(list);
		result.setPage(page.getPageNum());
		result.setSize(page.getPageSize());
		result.setTotal((int)page.getTotal());
		return result;
	}

	@Override
	public Message getDetail(int mid) {
		return msgDao.getDetail(mid);
	}

	@Override
	public void deleteMessage(int[] id) {
		msgDao.deleteMessage(id);
	}
}
