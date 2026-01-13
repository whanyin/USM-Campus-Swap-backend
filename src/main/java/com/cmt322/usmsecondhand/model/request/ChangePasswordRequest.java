// 创建 ChangePasswordRequest.java
package com.cmt322.usmsecondhand.model.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class ChangePasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String oldPassword;
    private String newPassword;
}