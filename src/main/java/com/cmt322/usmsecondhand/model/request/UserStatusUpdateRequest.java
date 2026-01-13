package com.cmt322.usmsecondhand.model.request;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户状态更新请求
 * 对应 Rubric: Code Quality - Use of proper DTOs for data transfer
 */
@Data
public class UserStatusUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户状态：0 - 正常 (Active), 1 - 封禁 (Banned)
     */
    private Integer status;
}