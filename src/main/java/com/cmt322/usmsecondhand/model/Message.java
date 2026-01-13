package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long conversationId;

    /**
     * 
     */
    private Long senderId;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Integer type;

    /**
     * 
     */
    private Date createTime;
}