package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.mapper.ConversationMapper;
import com.cmt322.usmsecondhand.model.Conversation;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.ConversationService;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.service.UserService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class ConversationServiceImpl
        extends ServiceImpl<ConversationMapper, Conversation>
        implements ConversationService {

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserService userService;

    @Override
    public Long open(Long goodsId, Long buyerId) {
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Item not found");
        }

        Long sellerId = goods.getUserId();

        if (buyerId.equals(sellerId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "You cannot chat with yourself regarding your own item.");
        }

        Conversation c = this.getOne(
                new QueryWrapper<Conversation>()
                        .eq("goodsId", goodsId)
                        .eq("buyerId", buyerId)
        );

        if (c != null) return c.getId();

        Conversation nc = new Conversation();
        nc.setGoodsId(goodsId);
        nc.setBuyerId(buyerId);
        nc.setSellerId(sellerId);
        this.save(nc);

        return nc.getId();
    }



    @Override
    public List<Conversation> listMy(Long currentUserId) {
        // 1. 查数据库，找出与我有关的会话
        QueryWrapper<Conversation> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("buyerId", currentUserId).or().eq("sellerId", currentUserId));
        queryWrapper.orderByDesc("lastTime"); // 按时间倒序
        List<Conversation> conversationList = this.list(queryWrapper);

        // 2. 遍历列表，填充对方的名字 (targetName)
        for (Conversation conv : conversationList) {
            Long targetId;

            // 如果我是卖家 -> 对方是买家
            if (conv.getSellerId().equals(currentUserId)) {
                targetId = conv.getBuyerId();
            } else {
                // 如果我是买家 -> 对方是卖家
                targetId = conv.getSellerId();
            }

            // 查询对方信息
            User targetUser = userService.getById(targetId);

            if (targetUser != null) {
                conv.setTargetName(targetUser.getUsername());
                conv.setTargetAvatar(targetUser.getAvatarUrl());
            } else {
                conv.setTargetName("Unknown User");
            }
        }

        return conversationList;
    }
}
