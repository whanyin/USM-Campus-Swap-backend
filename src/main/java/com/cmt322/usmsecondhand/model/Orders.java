package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Order Table
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders {
    /**
     * Primary Key ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Order Number
     */
    private String orderNo;

    /**
     * Goods ID
     */
    private Long goodsId;

    /**
     * Buyer User ID
     */
    private Long buyerId;

    /**
     * Seller User ID
     */
    private Long sellerId;

    /**
     * Quantity
     */
    private Integer quantity;

    /**
     * Total Amount
     */
    private BigDecimal totalAmount;

    /**
     * Delivery Method 1-Pickup 2-Express Delivery
     */
    private Integer deliveryMethod;

    /**
     * Pickup Location
     */
    private String pickupLocation;

    /**
     * Scheduled Pickup Time
     */
    private Date pickupTime;

    /**
     * Express Delivery Address
     */
    private String expressAddress;

    /**
     * Order Status 1-Pending 2-Paid 3-Shipped 4-Completed 5-Cancelled 6-Refunding
     */
    private Integer orderStatus;

    /**
     * Payment Method 1-Balance 2-Cash
     */
    private Integer paymentMethod;

    /**
     * Payment Time
     */
    private Date paymentTime;

    /**
     * Buyer Note
     */
    private String buyerNote;

    /**
     * Seller Note
     */
    private String sellerNote;

    /**
     * Create Time
     */
    private Date createTime;

    /**
     * Update Time
     */
    private Date updateTime;

    /**
     * Order Completion Time
     */
    private Date completeTime;
}