package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.Conversation;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.ConversationService;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.service.UserService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Resource
    private ConversationService conversationService;

    @Resource
    private UserService userService;

    @Resource
    private GoodsService goodsService;

    /**
     * 打开或创建一个聊天对话
     */
    @PostMapping("/open")
    public BaseResponse<Long> open(@RequestParam Long goodsId, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if (user == null) {
            return new BaseResponse<>(1, null, "User not logged in");
        }

        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return new BaseResponse<>(1, null, "Goods not found");
        }

        Long conversationId = conversationService.open(goodsId, user.getId());
        if (conversationId == null) {
            return new BaseResponse<>(1, null, "Failed to create conversation");
        }

        return new BaseResponse<>(0, conversationId, "Success");
    }


    /**
     * 获取当前用户的所有聊天对话
     */
    @GetMapping("/my")
    public BaseResponse<List<Conversation>> my(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 调用 Service 获取列表（Service 里负责填入 targetName）
        List<Conversation> conversationList = conversationService.listMy(user.getId());

        // ✅ 修改点：使用 ResultUtils 包装返回
        return ResultUtils.success(conversationList);
    }
}
