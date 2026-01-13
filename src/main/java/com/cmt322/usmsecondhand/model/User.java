package com.cmt322.usmsecondhand.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * USM Secondhand Trading Platform User Table
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Primary Key ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * User Nickname
     */
    private String username;

    /**
     * Login Account
     */
    private String userAccount;

    /**
     * Avatar URL
     */
    private String avatarUrl;

    /**
     * Gender 0-Unknown 1-Male 2-Female
     */
    private Integer gender;

    /**
     * Encrypted Password
     */
    private String userPassword;

    /**
     * Phone Number
     */
    private String phone;

    /**
     * USM Email (@usm.my)
     */
    private String usmEmail;

    /**
     * Email Verified 0-Not Verified 1-Verified
     */
    private Integer emailVerified;

    /**
     * Campus
     */
    private String campus;

    /**
     * Student ID
     */
    private String studentId;

    /**
     * school
     */
    private String school;

    /**
     * Account Balance
     */
    private BigDecimal balance;

    /**
     * User Status 0-Not Verified 1-Normal 2-Disabled
     */
    private Integer userStatus;

    /**
     * Create Time
     */
    private Date createTime;

    /**
     * Update Time
     */
    private Date updateTime;

    /**
     * Logical Delete 0-Not Deleted 1-Deleted
     */
    private Integer isDelete;

    /**
     * User Role 0-Normal User 1-Administrator
     */
    private Integer userRole;

    /**
     * Default address for express delivery
     */
    private String address;
}