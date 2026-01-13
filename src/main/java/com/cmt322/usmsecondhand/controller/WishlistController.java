package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.vo.WishlistVO;
import com.cmt322.usmsecondhand.service.UserService;
import com.cmt322.usmsecondhand.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Resource
    private WishlistService wishlistService;

    @Resource
    private UserService userService;

    // 1. 添加收藏
    @PostMapping("/add")
    public BaseResponse<Boolean> addWishlist(@RequestBody Map<String, Long> payload, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) throw new BusinessException(ErrorCode.NOT_LOGIN);

        Long goodsId = payload.get("goodsId");
        if (goodsId == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);

        boolean result = wishlistService.addWishlist(loginUser.getId(), goodsId);
        return ResultUtils.success(result);
    }

    // 2. 移除收藏
    @PostMapping("/remove")
    public BaseResponse<Boolean> removeWishlist(@RequestBody Map<String, Long> payload, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) throw new BusinessException(ErrorCode.NOT_LOGIN);

        Long goodsId = payload.get("goodsId");
        boolean result = wishlistService.removeWishlist(loginUser.getId(), goodsId);
        return ResultUtils.success(result);
    }

    // 3. 获取我的收藏列表 (组装数据)
    @GetMapping("/list")
    public BaseResponse<List<WishlistVO>> getMyWishlist(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) throw new BusinessException(ErrorCode.NOT_LOGIN);

        List<WishlistVO> list = wishlistService.getUserWishlist(loginUser.getId());
        return ResultUtils.success(list);
    }


    @GetMapping("/is-collected")
    public BaseResponse<Boolean> isCollected(@RequestParam Long goodsId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        // 如果用户没登录，肯定没收藏，返回 false
        if (loginUser == null) {
            return ResultUtils.success(false);
        }

        // 调用 Service 查询
        boolean isCollected = wishlistService.isCollected(loginUser.getId(), goodsId);
        return ResultUtils.success(isCollected);
    }

}