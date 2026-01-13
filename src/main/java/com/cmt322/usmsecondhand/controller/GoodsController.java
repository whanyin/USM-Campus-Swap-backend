package com.cmt322.usmsecondhand.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.GoodsPublishRequest;
import com.cmt322.usmsecondhand.model.request.GoodsUpdateRequest;
import com.cmt322.usmsecondhand.model.vo.GoodsVO;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品接口
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Resource
    private UserService userService;

    @Resource
    private GoodsService goodsService;

    /**
     * 发布商品
     */
    @PostMapping("/publish")
    public BaseResponse<Long> publishGoods(@RequestBody GoodsPublishRequest request, HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        long goodsId = goodsService.publishGoods(request, loginUser);
        return ResultUtils.success(goodsId);
    }

    /**
     * 更新商品
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateGoods(@RequestBody GoodsUpdateRequest request, HttpServletRequest httpRequest) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        boolean result = goodsService.updateGoods(request, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 搜索商品 / 获取商品列表
     * 支持关键词搜索和分类筛选
     */
    @GetMapping("/search")
    public BaseResponse<List<GoodsVO>> searchGoods(@RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) Long categoryId) {
        List<GoodsVO> list = goodsService.searchGoods(keyword, categoryId);
        return ResultUtils.success(list);
    }

    /**
     * 获取商品详情
     * (已修复：合并了之前的两个重复方法)
     */
    @GetMapping("/{id}")
    public BaseResponse<GoodsVO> getGoodsDetail(@PathVariable Long id, HttpServletRequest httpRequest) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GoodsVO goodsVO = goodsService.getGoodsDetail(id, httpRequest);
        return ResultUtils.success(goodsVO);
    }

    /**
     * 删除商品
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteGoods(@RequestBody long id, HttpServletRequest httpRequest) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        boolean isAdmin = userService.isAdmin(httpRequest);
        boolean result = goodsService.deleteGoods(id, loginUser, isAdmin);
        return ResultUtils.success(result);
    }

    @GetMapping("/list")
    public BaseResponse<IPage<Goods>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResultUtils.success(goodsService.listGoods(pageNum, pageSize, keyword, categoryId));
    }

    /**
     * 获取我发布的商品列表
     */
    @GetMapping("/my/list")
    public BaseResponse<List<GoodsVO>> listMyGoods(@RequestParam(required = false) Integer status, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<GoodsVO> list = goodsService.listMyGoods(loginUser, status);
        return ResultUtils.success(list);
    }

    /**
     * 更新商品状态 (比如标记为已售出)
     * status: 1-Active, 2-Sold, 3-OffShelf
     */
    @PostMapping("/status")
    public BaseResponse<Boolean> updateGoodsStatus(@RequestBody GoodsUpdateRequest request, HttpServletRequest httpRequest) {
        if (request == null || request.getId() == null || request.getStatus() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        boolean result = goodsService.updateStatus(request.getId(), request.getStatus(), loginUser);
        return ResultUtils.success(result);
    }
}