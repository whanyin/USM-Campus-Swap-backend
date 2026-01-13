package com.cmt322.usmsecondhand.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.constant.UserConstant;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.Orders;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.OrderCreateRequest;
import com.cmt322.usmsecondhand.model.request.OrderPayRequest;
import com.cmt322.usmsecondhand.model.vo.OrdersVO;
import com.cmt322.usmsecondhand.service.OrderService;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单接口控制器
 */
@RestController
@RequestMapping("/order") // ★★★ 核心修改：改成 /order 以匹配前端和 404 报错路径 ★★★
@Slf4j
public class OrdersController {

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    /**
     * 创建订单 (购买) - 单个商品流程
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
     * 查询我的订单 (买家/卖家)
     * @param role "buyer" or "seller"
     */
    @GetMapping("/list") // 前端可能是 /order/list 或者 /order/my/list，请根据你实际前端调用调整
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
     * 支付接口 (支持购物车合并支付)
     */
    @PostMapping("/pay")
    public BaseResponse<Boolean> doPayment(@RequestBody OrderPayRequest request, HttpServletRequest httpRequest) {
        User sessionUser = userService.getLoginUser(httpRequest);
        if (sessionUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 获取最新的用户信息（确保余额准确）
        User loginUser = userService.getById(sessionUser.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "User not found");
        }

        boolean result = orderService.processPayment(request, loginUser);

        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Payment failed");
        }

        // 更新 Session 中的用户信息 (余额变动)
        User updatedUser = userService.getById(loginUser.getId());
        httpRequest.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, updatedUser);

        return ResultUtils.success(true);
    }

    /**
     * ★★★ 管理员分页获取订单列表 ★★★
     * 对应前端: /api/order/list/page
     */
    @GetMapping("/list/page")
    public BaseResponse<IPage<OrdersVO>> listOrderPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {

        // 1. 安全检查：必须是管理员
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Admin access only");
        }

        // 2. 调用 Service
        IPage<OrdersVO> orderPage = orderService.listOrderVOByPage(current, size, orderNo, status);
        return ResultUtils.success(orderPage);
    }

    /**
     * ★★★ 管理员取消订单 ★★★
     * 对应前端 AdminOrderView 的取消按钮
     */
    @PostMapping("/cancel/{id}")
    public BaseResponse<Boolean> cancelOrder(@PathVariable("id") Long id, HttpServletRequest request) {
        // 1. 安全检查
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User loginUser = userService.getLoginUser(request);

        // 2. 调用 Service (复用现有的取消逻辑)
        boolean result = orderService.cancelOrder(id, loginUser);
        return ResultUtils.success(result);
    }
}