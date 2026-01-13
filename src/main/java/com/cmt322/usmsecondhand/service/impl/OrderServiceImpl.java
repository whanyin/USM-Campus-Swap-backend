package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.mapper.OrdersMapper;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.Orders;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.OrderCreateRequest;
import com.cmt322.usmsecondhand.model.request.OrderPayRequest; // ✅ 确保导入的是这个包含 itemIds 的类
import com.cmt322.usmsecondhand.model.vo.OrdersVO;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.service.OrderService;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrderService {

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserService userService;

    // --- ⬇️ 这个是旧的单商品创建方法，可以保留作为备用，或者删掉 ---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(OrderCreateRequest request, User loginUser) {
        Long goodsId = request.getGoodsId();
        if (goodsId == null || goodsId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Goods goods = goodsService.getById(goodsId);
        if (goods == null || goods.getStatus() != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Item is no longer available");
        }
        if (goods.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot buy your own item");
        }
        goods.setStatus(2);
        goodsService.updateById(goods);

        Orders order = new Orders();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setGoodsId(goodsId);
        order.setBuyerId(loginUser.getId());
        order.setSellerId(goods.getUserId());
        order.setTotalAmount(goods.getPrice());
        order.setQuantity(1);
        order.setDeliveryMethod(request.getDeliveryMethod());
        if (request.getDeliveryMethod() != null && request.getDeliveryMethod() == 2) {
            order.setExpressAddress(request.getAddressLocation());
        } else {
            order.setPickupLocation(request.getAddressLocation());
        }
        order.setPaymentMethod(request.getPaymentMethod());
        order.setBuyerNote(request.getBuyerNote());
        order.setOrderStatus(2); // Paid
        order.setCreateTime(new Date());
        this.save(order);
        return order;
    }

    // --- ⬇️ 查询订单列表 (保持不变) ---
    @Override
    public List<OrdersVO> listMyOrders(User loginUser, String role) {
        Long userId = loginUser.getId();
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        if ("buyer".equalsIgnoreCase(role)) {
            queryWrapper.eq("buyerId", userId);
        } else if ("seller".equalsIgnoreCase(role)) {
            queryWrapper.eq("sellerId", userId);
        } else {
            queryWrapper.and(wrapper -> wrapper.eq("buyerId", userId).or().eq("sellerId", userId));
        }

        queryWrapper.orderByDesc("createTime");
        List<Orders> ordersList = this.list(queryWrapper);

        return ordersList.stream().map(order -> {
            OrdersVO vo = OrdersVO.objToVo(order);
            fillOrderVO(vo, userId);
            return vo;
        }).collect(Collectors.toList());
    }

    // --- ⬇️ 订单详情 (保持不变) ---
    @Override
    public OrdersVO getOrderDetail(long orderId, User loginUser) {
        Orders order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long userId = loginUser.getId();
        if (!order.getBuyerId().equals(userId) &&
                !order.getSellerId().equals(userId) &&
                !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        OrdersVO vo = OrdersVO.objToVo(order);
        fillOrderVO(vo, userId);
        return vo;
    }

    // --- ⬇️ 取消订单 (保持不变) ---
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(long orderId, User loginUser) {
        Orders order = this.getById(orderId);
        if (order == null) return false;

        if (!order.getBuyerId().equals(loginUser.getId()) && !order.getSellerId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (order.getOrderStatus() >= 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot cancel completed order");
        }

        order.setOrderStatus(5); // Cancelled
        boolean updateOrder = this.updateById(order);

        Goods goods = goodsService.getById(order.getGoodsId());
        if (goods != null) {
            goods.setStatus(1); // Back to Available
            goodsService.updateById(goods);
        }
        return updateOrder;
    }

    // --- ⬇️ 完成订单 (保持不变) ---
    @Override
    public boolean completeOrder(long orderId, User loginUser) {
        Orders order = this.getById(orderId);
        if (order == null) return false;
        if (!order.getBuyerId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        order.setOrderStatus(4); // Completed
        order.setCompleteTime(new Date());
        return this.updateById(order);
    }

    // --- ⬇️ 辅助填充 (保持不变) ---
    private void fillOrderVO(OrdersVO vo, Long currentUserId) {
        Goods goods = goodsService.getById(vo.getGoodsId());
        if (goods != null) {
            vo.setGoodsTitle(goods.getTitle());
            if (goods.getCoverImage() != null) {
                vo.setGoodsImage(goods.getCoverImage());
            } else if (goods.getImages() != null) {
                vo.setGoodsImage(goods.getImages().toString());
            }
        }
        Long counterpartyId = vo.getBuyerId().equals(currentUserId) ? vo.getSellerId() : vo.getBuyerId();
        User user = userService.getById(counterpartyId);
        if (user != null) {
            vo.setCounterpartyName(user.getUsername());
            vo.setCounterpartyAvatar(user.getAvatarUrl());
        }
    }

    /**
     * ✅ 核心修改：处理支付 (支持批量)
     * 对应前端 Payment 页面
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务，任何一步报错全部回滚
    public boolean processPayment(OrderPayRequest request, User buyer) {
        // 1. 获取参数
        List<Long> itemIds = request.getItemIds();
        Integer payMethod = request.getPayMethod();
        BigDecimal amount = request.getAmount();

        if (itemIds == null || itemIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "No items selected");
        }

        // 2. 批量查询商品
        List<Goods> goodsList = goodsService.listByIds(itemIds);
        if (goodsList.size() != itemIds.size()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Some items are invalid or removed");
        }

        // 3. 校验商品状态
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        for (Goods goods : goodsList) {
            if (goods.getStatus() != 1) { // 必须是 1-Available
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Item '" + goods.getTitle() + "' is already sold");
            }
            if (goods.getUserId().equals(buyer.getId())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot buy your own item: " + goods.getTitle());
            }
            calculatedTotal = calculatedTotal.add(goods.getPrice());
        }

        // 4. (可选) 校验前端传来的金额和后端计算的是否一致
        // if (calculatedTotal.compareTo(amount) != 0) { ... }

        // 5. 如果是余额支付，扣款
        if (request.getPayMethod() == 1) { // 1 = Wallet Balance
            if (buyer.getBalance().compareTo(amount) < 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "Insufficient wallet balance");
            }
            // 扣除余额
            buyer.setBalance(buyer.getBalance().subtract(amount));
            userService.updateById(buyer);
        }

        // 6. 循环处理每个商品：下架 + 生成订单
        for (Goods goods : goodsList) {
            // 6.1 下架商品 (Status -> 2 Sold)
            goods.setStatus(2);
            boolean updateGoods = goodsService.updateById(goods);
            if (!updateGoods) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to update goods status");
            }

            // 6.2 生成订单
            Orders order = new Orders();
            String orderNo = "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            order.setOrderNo(orderNo);
            order.setGoodsId(goods.getId());
            order.setBuyerId(buyer.getId());
            order.setSellerId(goods.getUserId());
            order.setTotalAmount(goods.getPrice()); // 这里存单价
            order.setQuantity(1);
            order.setPaymentMethod(payMethod);

            // 状态设置：余额支付=已支付(2)，现金支付=待支付/待交易(1)
            order.setOrderStatus(payMethod == 1 ? 2 : 1);
            if (payMethod == 1) {
                order.setPaymentTime(new Date());
            }
            order.setCreateTime(new Date());

            // 6.3 保存订单
            this.save(order);
        }

        return true;
    }
}