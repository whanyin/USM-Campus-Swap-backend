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
    SYSTEM_ERROR(50000, "Internal server error", "");





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
