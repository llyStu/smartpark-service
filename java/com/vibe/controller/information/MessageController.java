package com.vibe.controller.information;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vibe.pojo.user.User;
import com.vibe.pojo.infomation.Message;
import com.vibe.pojo.infomation.MessageVo;
import com.vibe.service.information.MessageService;
import com.vibe.utils.Page;

@Controller
public class MessageController {
    @Autowired
    private MessageService msgService;

    @RequestMapping("/information/msg")
    public String index() {
        return "information/message";
    }

    @RequestMapping("/information/msg_search_listHtml")
    public String search_listHtml(MessageVo vo, HttpSession session, Model model) {
        Page<Message> msgPage = getSearchList(vo, session);
        model.addAttribute("msgPage", msgPage);
        return "information/message-list";
    }

    @RequestMapping("/information/msg_detail_html")
    public String detailHtml(int mid, HttpSession session, Model model) {
        Message m = detail(mid, session);
        model.addAttribute("msg", m);
        return "information/message-detail";
    }

    @RequestMapping("/information/msg_detail")
    @ResponseBody
    public Message detail(int mid, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Message m = msgService.getDetail(mid);
        if (user.getId().equals(m.getReceiver()) || user.getId().equals(m.getSender())) {
            return m;
        }
        return null;
    }

    @RequestMapping("/information/msg_send_jsp")
    public ModelAndView sendMessage_jsp(Integer receiver, String content, HttpSession session) {
        sendMessage(receiver, content, session);
        return new ModelAndView("redirect:/information/msg");
    }

    @RequestMapping("/information/msg_send")
    @ResponseBody
    public String sendMessage(Integer receiver, String content, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");

        Message message = new Message();
        message.setSender(user.getId());
        message.setReceiver(receiver);
        message.setContent(content);
        msgService.sendMessage(message);
        return "SUCCESS";
    }

    @RequestMapping("/information/msg_search")
    @ResponseBody
    public Page<Message> getSearchList(MessageVo vo, HttpSession session) {
        if (vo.getUser() != null) switch (vo.getUsertype()) {
            case 1:
                vo.setSender(vo.getUser());
                vo.setUser(null);
                break;
            case 2:
                vo.setReceiver(vo.getUser());
                vo.setUser(null);
                break;
        }
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        int pageSize = vo.getPageSize() == null ? 10 : vo.getPageSize();

        Page<Message> message = msgService.getSearchMessages(vo, pageNum, pageSize);
        return message;
    }

    @RequestMapping("/information/msg_delete")
    @ResponseBody
    public String deleteMessage(int[] id, HttpSession session) {
        if (id != null && id.length != 0) {
            msgService.deleteMessage(id);
        }
        return "OK";
    }
}
