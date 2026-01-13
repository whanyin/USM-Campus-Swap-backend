package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.constant.UserConstant;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.mapper.UserMapper;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cmt322.usmsecondhand.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 米老头
* @description 针对表【user(USM Secondhand Trading Platform User Table)】的数据库操作Service实现
* @createDate 2025-11-22 14:08:16
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;


    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "cmt322";

    @Override
    public long userRegister(String username, String userAccount, String userPassword,
                             String checkPassword, String usmEmail, String campus,
                             String studentId, String school, String phone) {
        // Validation
        if (StringUtils.isAnyBlank(username, userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Required parameters cannot be empty");
        }
        if (username.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Nickname cannot exceed 20 characters");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account is too short");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password is too short");
        }
        // Account cannot contain special characters
        String regex = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account contains special characters");
        }
        // Passwords must match
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Passwords do not match");
        }
        // USM email validation
        if (!usmEmail.toLowerCase().endsWith(".usm.my")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Please use USM email (@usm.my) to register");
        }
        // Email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, usmEmail)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid email format");
        }

        // Phone number format validation (if provided)
        if (StringUtils.isNotBlank(phone)) {
            if (phone.length() < 12 || phone.length() > 13) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid phone number format");
            }


            // Verify if the rest are pure numbers
            String numberPart = phone.substring(3);
            if (!numberPart.matches("^[0-9]+$")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Phone number contains invalid characters");
            }
        }

        // Student ID format validation (if provided)
        if (StringUtils.isNotBlank(studentId)) {
            if (studentId.length() < 6) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid student ID format");
            }
        }

        // Account cannot be duplicated
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account already exists");
        }

        // USM email cannot be duplicated
        QueryWrapper<User> emailWrapper = new QueryWrapper<>();
        emailWrapper.eq("usmEmail", usmEmail);
        long emailCount = this.count(emailWrapper);
        if (emailCount > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "This USM email is already registered");
        }

        // Student ID cannot be duplicated (if provided)
        if (StringUtils.isNotBlank(studentId)) {
            QueryWrapper<User> studentIdWrapper = new QueryWrapper<>();
            studentIdWrapper.eq("studentId", studentId);
            long studentIdCount = this.count(studentIdWrapper);
            if (studentIdCount > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "This student ID is already registered");
            }
        }

        // Phone number cannot be duplicated (if provided)
        if (StringUtils.isNotBlank(phone)) {
            QueryWrapper<User> phoneWrapper = new QueryWrapper<>();
            phoneWrapper.eq("phone", phone);
            long phoneCount = this.count(phoneWrapper);
            if (phoneCount > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "This phone number is already registered");
            }
        }

        // Encryption
        String dealPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUsername(username);
        user.setUserAccount(userAccount);
        user.setUserPassword(dealPassword);
        user.setUsmEmail(usmEmail);
        user.setEmailVerified(1); // Initial state: not verified
        user.setCampus(campus);
        user.setStudentId(studentId);
        user.setSchool(school);
        user.setPhone(phone);
        user.setBalance(new BigDecimal("1000.00"));
        user.setGender(0); // Default: unknown gender
        user.setUserStatus(1); // Normal status
        user.setUserRole(0); // Normal user
        user.setAddress(""); // Empty address, can be set later
        user.setIsDelete(0);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Registration failed");
        }
        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验逻辑 (任何一项失败，直接抛出异常，不要返回 null)
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Username or password cannot be empty.");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account length must be at least 4 characters.");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password length must be at least 8 characters");
        }

        // 校验账户特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The account contains illegal characters.");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        // 用户不存在或密码错误
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            // 这里必须抛出异常，告诉前端具体原因
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Incorrect username or password");
        }

        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);

        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();

        // 基本信息
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setUsmEmail(originUser.getUsmEmail());

        safetyUser.setEmailVerified(originUser.getEmailVerified());
        safetyUser.setBalance(originUser.getBalance());
        safetyUser.setAddress(originUser.getAddress());

        // 学校信息
        safetyUser.setCampus(originUser.getCampus());
        safetyUser.setSchool(originUser.getSchool());
        safetyUser.setStudentId(originUser.getStudentId());

        // 系统信息
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());

        safetyUser.setUpdateTime(originUser.getUpdateTime());

        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 补充校验，如果用户没有传任何要更新的值，就直接报错，不用执行 update 语句
        // 如果是管理员，允许更新任意用户
        // 如果不是管理员，只允许更新当前（自己的）信息
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return (User) userObj;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    /**
     * 是否为管理员
     *
     * @param loginUser
     * @return
     */
    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    // 在 UserServiceImpl 中实现
    // UserServiceImpl.java
    @Override
    public boolean updatePassword(String oldPassword, String newPassword, User loginUser) {
        // 1. 参数校验
        if (StringUtils.isAnyBlank(oldPassword, newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password cannot be empty");
        }

        // 2. 新旧密码不能相同
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "New password cannot be the same as old password");
        }

        // 3. 新密码长度验证（至少8位）
        if (newPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "New password must be at least 8 characters");
        }

        // 4. 验证旧密码
        Long userId = loginUser.getId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "User not found");
        }

        String encryptedOldPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        if (!user.getUserPassword().equals(encryptedOldPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Old password is incorrect");
        }

        // 5. 加密新密码并更新
        String encryptedNewPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        user.setUserPassword(encryptedNewPassword);

        return this.updateById(user);
    }

}




