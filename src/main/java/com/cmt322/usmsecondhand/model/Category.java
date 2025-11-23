package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * Product Category Table
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category {
    /**
     * Primary Key ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Category Name
     */
    private String name;

    /**
     * Status 1-Active 0-Inactive
     */
    private Integer status;

    /**
     * Create Time
     */
    private Date createTime;
}