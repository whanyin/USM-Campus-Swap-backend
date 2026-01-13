package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.model.Message;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.MessageService;
import com.cmt322.usmsecondhand.service.UserService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public Boolean send(@RequestBody Message msg, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        msg.setSenderId(user.getId());
        return messageService.send(msg);
    }

    /**
     * 获取聊天记录
     */
    @GetMapping("/list")
    public List<Message> list(@RequestParam Long conversationId) {
        return messageService.list(conversationId);
    }
}
