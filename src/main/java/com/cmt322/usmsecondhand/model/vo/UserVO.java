package com.cmt322.usmsecondhand.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户包装类（脱敏）
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    private long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
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
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    private static final long serialVersionUID = 1L;
}