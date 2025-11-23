package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Goods Table
 * @TableName goods
 */
@TableName(value ="goods")
@Data
public class Goods {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private Long categoryId;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String coverImage;

    /**
     * Product images list
     */
    private Object images;

    /**
     * Product condition 1-New 2-Like New 3-Gently Used 4-Used
     */
    private Integer condition;

    /**
     * Status 1-Available 2-Sold 3-Inactive
     */
    private Integer status;

    /**
     * 
     */
    private Integer viewCount;

    /**
     * 
     */
    private Integer likeCount;

    /**
     * 
     */
    private String campus;

    /**
     * Contact method 1-In-app message 2-Phone 3-Email
     */
    private Integer contactType;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer isDelete;
}