package com.cmt322.usmsecondhand.common;

/**
 * 全局错误码
 */

public enum ErrorCode {


    SUCCESS(0, "ok", ""),
    INFO_ERROR(30001, "Information Abnormality", "1"),
    PARAMS_ERROR(40000, "Incorrect request parameters", ""),
    NULL_ERROR(40001, "Request data cannot be empty", ""),
    NOT_LOGIN(40100, "Please login first", ""),
    NO_AUTH(40101, "Access denied", ""),
    OPERATION_ERROR(40111, "Operation error", ""),
    SYSTEM_ERROR(50000, "Internal server error", ""),
    // 在 ErrorCode 枚举中添加
    EMAIL_SEND_ERROR(40301, "Failed to send email", "Failed to send email"),
    EMAIL_CONNECT_ERROR(40301, "Failed connect to email server", "Failed to connect to email server"),
    EMAIL_VERIFY_ERROR(40302, "Email verification failed", "Email verification failed"),
    EMAIL_LIMIT_ERROR(40303, "Email sending limit exceeded", "Please wait before sending another email"),
    EMAIL_CODE_ERROR(40304, "Invalid verification code", "Verification code is invalid or expired"),
    EMAIL_FORMAT_ERROR(40305, "Invalid email format", "Please enter a valid email address"),
    EMAIL_NOT_MATCH(40306, "Email does not match user account", "Email does not belong to current user");;






    /**
     * 状态码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;
    /**
     * 错误详情描述
     */
    private final String description;

     ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
