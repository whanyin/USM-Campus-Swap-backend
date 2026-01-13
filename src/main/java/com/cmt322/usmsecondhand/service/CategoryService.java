package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Category;
import com.cmt322.usmsecondhand.model.User;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 获取所有有效分类 (用于首页展示和发布商品下拉框)
     */
    List<Category> listAllCategories();

    /**
     * 添加分类 (仅管理员)
     */
    long addCategory(String name, User loginUser);

    /**
     * 删除分类 (仅管理员)
     */
    boolean deleteCategory(long id, User loginUser);
}