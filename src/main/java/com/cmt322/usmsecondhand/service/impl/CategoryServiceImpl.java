package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.mapper.CategoryMapper;
import com.cmt322.usmsecondhand.model.Category;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.service.CategoryService;
import com.cmt322.usmsecondhand.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private UserService userService;

    @Override
    public List<Category> listAllCategories() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        // 只查有效的分类 (Status = 1)
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("createTime");
        return this.list(queryWrapper);
    }

    @Override
    public long addCategory(String name, User loginUser) {
        // 1. 权限校验 (Report B Security)
        if (!userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Only admins can manage categories");
        }

        // 2. 参数校验
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 3. 判重
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Category already exists");
        }

        Category category = new Category();
        category.setName(name);
        category.setStatus(1); // 默认启用

        boolean result = this.save(category);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return category.getId();
    }

    @Override
    public boolean deleteCategory(long id, User loginUser) {
        // 1. 权限校验
        if (!userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 逻辑删除或物理删除，这里直接物理删除
        return this.removeById(id);
    }
}