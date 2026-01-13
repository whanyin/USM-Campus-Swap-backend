package com.cmt322.usmsecondhand.model.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long itemId;      // 对应前端 query.itemId
    private Integer payMethod; // 1-Balance, 2-Cash
    private BigDecimal amount; // 校验金额
}