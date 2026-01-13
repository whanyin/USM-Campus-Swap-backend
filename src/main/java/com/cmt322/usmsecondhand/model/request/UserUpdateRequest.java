package com.cmt322.usmsecondhand.model.request; // 👈 注意包名，要和你实际的路径一致

import lombok.Data;
import java.io.Serializable;

/**
 * 用户更新请求体
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像 (✅ 必须有这个，否则头像上传后存不进去)
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
     * 邮箱
     */
    private String usmEmail;

    /**
     * 校区
     */
    private String campus;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 学院/学校
     */
    private String school;

    /**
     * 地址
     */
    private String address;
}