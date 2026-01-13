package com.cmt322.usmsecondhand.model.vo;

import com.cmt322.usmsecondhand.model.Orders;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 订单展示对象 (包含商品和对方信息)
 */
@Data
public class OrdersVO extends Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    // 商品快照信息
    private String goodsTitle;
    private String goodsImage;

    // 卖家/买家信息 (视当前查看者身份而定)
    private String counterpartyName; // 交易对手名字
    private String counterpartyAvatar;

    public static OrdersVO objToVo(Orders orders) {
        if (orders == null) return null;
        OrdersVO vo = new OrdersVO();
        BeanUtils.copyProperties(orders, vo);
        return vo;
    }
}