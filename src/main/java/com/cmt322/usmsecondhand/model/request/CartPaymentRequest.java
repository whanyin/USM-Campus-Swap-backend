package com.cmt322.usmsecondhand.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CartPaymentRequest {
    private List<Long> itemIds; // 购物车里选中的所有商品 ID
    private Integer payMethod;  // 1-Balance, 2-Cash
}