package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.mapper.ConversationMapper;
import com.cmt322.usmsecondhand.mapper.MessageMapper;
import com.cmt322.usmsecondhand.model.Conversation;
import com.cmt322.usmsecondhand.model.Message;
import com.cmt322.usmsecondhand.service.MessageService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl
        extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Resource
    private ConversationMapper conversationMapper;

    @Override
    public Boolean send(Message msg) {
        this.save(msg);

        // 更新 conversation 的最后消息
        conversationMapper.update(null,
                new UpdateWrapper<Conversation>()
                        .eq("id", msg.getConversationId())
                        .set("lastMessage", msg.getContent())
                        .set("lastTime", new Date())
        );

        return true;
    }

    @Override
    public List<Message> list(Long cid) {
        return this.list(
                new QueryWrapper<Message>()
                        .eq("conversationId", cid)
                        .orderByAsc("id")
        );
    }
}
