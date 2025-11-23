package com.cmt322.usmsecondhand.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 3688259211110907982L;
    private  String userName;
    private  String userAccount;
    private  String userPassword;
    private  String checkPassword;
    private  String usmEmail;
    private  String campus;
    private  String studentId;
    private  String school;
    private  String phone;
}
