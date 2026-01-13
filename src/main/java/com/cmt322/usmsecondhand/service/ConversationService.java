package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Conversation;

import java.util.List;

public interface ConversationService extends IService<Conversation> {

    Long open(Long goodsId, Long buyerId);

    List<Conversation> listMy(Long userId);

}
