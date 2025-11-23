package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.model.vo.GoodsVO;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Resource
    private UserService userService;

    @Resource
    private  GoodsService goodsService;

    // 发布商品
    @PostMapping("/publish")
    public BaseResponse<Long> publishGoods(@RequestBody Goods goods, HttpServletRequest request) {
        if (goods == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser  = userService.getLoginUser(request);
        Goods goods = new Goods();

        long goodsId = goodsService.publishGoods(goods, loginUser);
        return ResultUtils.success(goodsId);
    }
}
