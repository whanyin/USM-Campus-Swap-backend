package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Message;

import java.util.List;

public interface MessageService extends IService<Message> {

    Boolean send(Message message);

    List<Message> list(Long conversationId);
}
