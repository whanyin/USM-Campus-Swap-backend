package com.cmt322.usmsecondhand.constant;

public class GoodsConstants {

    // 商品状态
    public static final Integer STATUS_AVAILABLE = 1;
    public static final Integer STATUS_SOLD = 2;
    public static final Integer STATUS_INACTIVE = 3;

    // 商品成色
    public static final Integer CONDITION_NEW = 1;
    public static final Integer CONDITION_LIKE_NEW = 2;
    public static final Integer CONDITION_GENTLY_USED = 3;
    public static final Integer CONDITION_USED = 4;

    // 联系方式
    public static final Integer CONTACT_IN_APP = 1;
    public static final Integer CONTACT_PHONE = 2;
    public static final Integer CONTACT_EMAIL = 3;

    // 配送方式
    public static final Integer DELIVERY_SELF_PICKUP = 1;     // 自取
    public static final Integer DELIVERY_EXPRESS = 2;         // 快递
    public static final Integer DELIVERY_BOTH = 3;            // 两种都支持
}