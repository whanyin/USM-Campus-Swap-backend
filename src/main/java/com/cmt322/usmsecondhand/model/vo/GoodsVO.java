package com.cmt322.usmsecondhand.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsVO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Long userId;
    private String coverImage;
    private Object images;
    private Integer condition;
    private Integer status;
    private Integer viewCount;
    private Integer likeCount;
    private String campus;
    private Integer contactType;
    private Date createTime;
    private Date updateTime;

    // 关联信息
    private String userName;
    private String userAvatar;
    private String categoryName;
}