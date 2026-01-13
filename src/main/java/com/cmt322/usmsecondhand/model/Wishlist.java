package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 商品收藏表
 * @TableName wishlist
 */
@TableName(value ="wishlist")
@Data
public class Wishlist {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long goodsId;

    /**
     * 收藏时间
     */
    private Date createTime;
}