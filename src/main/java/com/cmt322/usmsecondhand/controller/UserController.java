package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.UserLoginRequest;
import com.cmt322.usmsecondhand.model.request.UserRegisterRequest;
import com.cmt322.usmsecondhand.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import static com.cmt322.usmsecondhand.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author hanyin
 */
@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUserName();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String usmEmail = userRegisterRequest.getUsmEmail();
        String campus   = userRegisterRequest.getCampus();
        String studentId = userRegisterRequest.getStudentId();
        String school = userRegisterRequest.getSchool();
        String phone = userRegisterRequest.getPhone();
        if (StringUtils.isAnyBlank(username, userAccount, userPassword, checkPassword,usmEmail,campus,studentId,school,phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(username, userAccount, userPassword, checkPassword,usmEmail,campus,studentId,school,phone);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法

        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request) {
        //1.校验参数是否为空
        if(user ==null)
        {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(user,loginUser);
        return ResultUtils.success(result);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

}
