package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName conversation
 */
@TableName(value ="conversation")
@Data
public class Conversation {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long goodsId;

    /**
     * 
     */
    private Long buyerId;

    /**
     * 
     */
    private Long sellerId;

    /**
     * 
     */
    private String lastMessage;

    /**
     * 
     */
    private Date lastTime;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private String targetName;

    @TableField(exist = false)
    private String targetAvatar;
}