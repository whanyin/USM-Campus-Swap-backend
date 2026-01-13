package com.cmt322.usmsecondhand.model.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WishlistVO {
    private Long goodsId;       // 商品ID (用于跳转详情/删除)
    private String title;       // 商品标题
    private BigDecimal price;   // 价格
    private String coverImage;  // 封面图
    private String campus;      // 校区
    private Integer viewCount;  // 浏览量
    
    // 卖家信息
    private Long sellerId;      // 卖家ID (用于联系卖家)
    private String sellerName;  // 卖家名字
    private String sellerAvatar;// 卖家头像
    
    private Date createTime;    // 收藏时间
}