package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Orders;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.OrderCreateRequest;
import com.cmt322.usmsecondhand.model.request.OrderPayRequest; // ✅ 确保引入了这个新的 DTO
import com.cmt322.usmsecondhand.model.vo.OrdersVO;

import java.util.List;

public interface OrderService extends IService<Orders> {

    /**
     * 创建订单 (单个商品详细购买流程 - 备用)
     */
    Orders createOrder(OrderCreateRequest request, User loginUser);

    /**
     * 查询我的订单列表
     * @param role "buyer" or "seller"
     */
    List<OrdersVO> listMyOrders(User loginUser, String role);

    /**
     * 获取订单详情
     */
    OrdersVO getOrderDetail(long orderId, User loginUser);

    /**
     * 取消订单
     */
    boolean cancelOrder(long orderId, User loginUser);

    /**
     * 完成订单 (确认收货)
     */
    boolean completeOrder(long orderId, User loginUser);

    /**
     * ✅ 核心修改：处理支付 (支持批量下单)
     * 1. 入参改为 OrderPayRequest (包含 itemIds 列表)
     * 2. 返回值改为 boolean (成功/失败)
     */
    boolean processPayment(OrderPayRequest request, User buyer);
}