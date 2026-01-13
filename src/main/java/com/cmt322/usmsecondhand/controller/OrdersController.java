package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.constant.UserConstant;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.Orders;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.OrderCreateRequest;
import com.cmt322.usmsecondhand.model.request.OrderPayRequest; // ✅ 引入新的 DTO
import com.cmt322.usmsecondhand.model.vo.OrdersVO;
import com.cmt322.usmsecondhand.service.OrderService;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// ⚠️ 注意：前端请求的是 /order/pay，所以这里建议改成 /order，或者去前端把请求路径改成 /orders/pay
@RequestMapping("/orders")
@Slf4j
public class OrdersController {

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    /**
     * 创建订单 (购买) - 单个商品流程 (备用)
     */
    @PostMapping("/create")
    public BaseResponse<Long> createOrder(@RequestBody OrderCreateRequest request, HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        Orders order = orderService.createOrder(request, loginUser);
        return ResultUtils.success(order.getId());
    }

    /**
     * 查询我的订单
     * @param role "buyer" or "seller"
     */
    @GetMapping("/list")
    public BaseResponse<List<OrdersVO>> listOrders(@RequestParam(required = false) String role, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<OrdersVO> list = orderService.listMyOrders(loginUser, role);
        return ResultUtils.success(list);
    }

    /**
     * 确认收货 / 完成订单
     */
    @PostMapping("/complete")
    public BaseResponse<Boolean> completeOrder(@RequestBody Long orderId, HttpServletRequest request) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = orderService.completeOrder(orderId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * ✅ 核心修改：支付接口
     */
    @PostMapping("/pay")
    public BaseResponse<Boolean> doPayment(@RequestBody OrderPayRequest request, HttpServletRequest httpRequest) {
        // 1. 从 Session 获取登录状态（只用来证明“你是谁”）
        User sessionUser = userService.getLoginUser(httpRequest);
        if (sessionUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        User loginUser = userService.getById(sessionUser.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "User not found");
        }

        boolean result = orderService.processPayment(request, loginUser);

        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Payment failed");
        }

        User updatedUser = userService.getById(loginUser.getId());
        httpRequest.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, updatedUser);

        return ResultUtils.success(true);
    }
}