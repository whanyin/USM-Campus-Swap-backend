package com.cmt322.usmsecondhand.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 3688259211110907982L;
    private  String userAccount;
    private  String userPassword;

}
