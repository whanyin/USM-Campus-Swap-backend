package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 米老头
* @description 针对表【user(USM Secondhand Trading Platform User Table)】的数据库操作Service
* @createDate 2025-11-22 14:08:16
*/
public interface UserService extends IService<User> {

    /**
     *用户注册
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String username, String userAccount, String userPassword,
                      String checkPassword, String usmEmail, String campus,
                      String studentId, String school, String phone);

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @return User
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return safeUser
     */
    User getSafetyUser(User originUser);

    /**
     * 退出登录
     * @return
     */
    int userLogout(HttpServletRequest request);


    /**
     * 更新用户
     * @param user
     * @return
     */
    int updateUser(User user,User loginUser);

    /**
     * 获取当前登录用户信息
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否位管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);


    /**
     * 是否位管理员
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    boolean updatePassword(String oldPassword, String newPassword, User loginUser);



}
