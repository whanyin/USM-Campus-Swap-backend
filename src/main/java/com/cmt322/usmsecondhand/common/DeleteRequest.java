package com.cmt322.usmsecondhand.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求
 */
@Data
public class DeleteRequest implements Serializable {



    private long id;
}
