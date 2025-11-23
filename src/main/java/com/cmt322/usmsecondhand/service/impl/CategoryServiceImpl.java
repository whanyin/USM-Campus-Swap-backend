package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.model.Category;
import com.cmt322.usmsecondhand.service.CategoryService;
import com.cmt322.usmsecondhand.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 米老头
* @description 针对表【category(Product Category Table)】的数据库操作Service实现
* @createDate 2025-11-22 14:08:39
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




