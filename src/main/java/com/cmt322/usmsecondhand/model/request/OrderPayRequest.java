package com.cmt322.usmsecondhand.model.request;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderPayRequest implements Serializable {
    /**
     * 购买的商品 ID 列表
     */
    private List<Long> itemIds;

    /**
     * 支付方式: 1-余额(Balance), 2-货到付款(Cash)
     */
    private Integer payMethod;

    /**
     * 总金额
     */
    private BigDecimal amount;
}