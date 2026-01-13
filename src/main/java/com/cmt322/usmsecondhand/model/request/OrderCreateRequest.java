package com.cmt322.usmsecondhand.model.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderCreateRequest implements Serializable {
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 交付方式 1-自提 2-快递
     */
    private Integer deliveryMethod;

    /**
     * 支付方式 1-余额 2-现金/面交支付
     */
    private Integer paymentMethod;

    /**
     * 备注
     */
    private String buyerNote;

    /**
     * 收货地址或面交地点
     */
    private String addressLocation;
}