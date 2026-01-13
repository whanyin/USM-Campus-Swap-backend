package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    @TableField(value = "categoryId")
    private Long categoryId;

    @TableField(value = "userId")
    private Long userId;

    @TableField(value = "coverImage")
    private String coverImage;

    /**
     * Product images list
     * 已修改为 String，对应数据库 json 类型
     */
    private String images;

    /**
     * Product condition
     * condition 是数据库保留字，必须加反引号
     */
    @TableField(value = "`condition`")
    private Integer condition;

    /**
     * Status
     */
    private Integer status;

    /**
     * 下面这些字段如果不加注解，MP 会去数据库找 view_count，导致查不到数据
     * 必须加上 @TableField(value = "viewCount")
     */
    @TableField(value = "viewCount")
    private Integer viewCount;

    @TableField(value = "likeCount")
    private Integer likeCount;

    private String campus;

    @TableField(value = "contactType")
    private Integer contactType;

    @TableField(value = "createTime")
    private Date createTime;

    @TableField(value = "updateTime")
    private Date updateTime;

    @TableField(value = "isDelete")
    private Integer isDelete;
}