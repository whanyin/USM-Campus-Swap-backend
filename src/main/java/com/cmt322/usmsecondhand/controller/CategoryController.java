package com.cmt322.usmsecondhand.controller;

import com.cmt322.usmsecondhand.common.BaseResponse;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.common.ResultUtils;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.model.Category;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.CategoryService;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 分类接口
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private UserService userService;

    /**
     * 获取所有分类 (公开接口)
     */
    @GetMapping("/list")
    public BaseResponse<List<Category>> listCategories() {
        List<Category> list = categoryService.listAllCategories();
        return ResultUtils.success(list);
    }

    /**
     * 添加分类 (管理员)
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCategory(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String name = payload.get("name");
        if (name == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long id = categoryService.addCategory(name, loginUser);
        return ResultUtils.success(id);
    }

    /**
     * 删除分类 (管理员)
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCategory(@RequestBody Map<String, Long> payload, HttpServletRequest request) {
        Long id = payload.get("id");
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = categoryService.deleteCategory(id, loginUser);
        return ResultUtils.success(result);
    }
}