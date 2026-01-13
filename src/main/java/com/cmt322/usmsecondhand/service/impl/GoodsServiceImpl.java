package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.common.ErrorCode;
import com.cmt322.usmsecondhand.exception.BusinessException;
import com.cmt322.usmsecondhand.mapper.GoodsMapper;
import com.cmt322.usmsecondhand.model.Category;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.GoodsPublishRequest;
import com.cmt322.usmsecondhand.model.request.GoodsUpdateRequest;
import com.cmt322.usmsecondhand.model.vo.GoodsVO;
import com.cmt322.usmsecondhand.model.vo.UserVO;
import com.cmt322.usmsecondhand.service.CategoryService;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.service.UserService;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Resource
    private UserService userService;

    @Resource
    private CategoryService categoryService;

    // 用于 List<String> 和 JSON String 之间的转换
    private final Gson gson = new Gson();

    @Override
    public long publishGoods(GoodsPublishRequest request, User loginUser) {
        // 1. 参数校验
        if (request == null || StringUtils.isAnyBlank(request.getTitle(), request.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (request.getPrice() == null || request.getPrice().doubleValue() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Price cannot be negative");
        }

        // 2. 转换对象
        Goods goods = new Goods();
        BeanUtils.copyProperties(request, goods);

        // 设置默认值
        goods.setUserId(loginUser.getId());
        goods.setStatus(1); // 1-Available
        goods.setViewCount(0);
        goods.setLikeCount(0);

        // 3. 处理图片逻辑 (List<String> -> JSON String)
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            // A. 将 List 转为 JSON 存入 images 字段
            goods.setImages(gson.toJson(request.getImages()));

            // B. 如果封面图为空，自动取第一张图作为封面
            if (StringUtils.isBlank(goods.getCoverImage())) {
                goods.setCoverImage(request.getImages().get(0));
            }
        }

        // 4. 处理联系方式
        if (request.getContactTypes() != null && !request.getContactTypes().isEmpty()) {
            goods.setContactType(request.getContactTypes().get(0));
        }

        boolean result = this.save(goods);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return goods.getId();
    }

    @Override
    public boolean updateGoods(GoodsUpdateRequest request, User loginUser) {
        Goods goods = this.getById(request.getId());
        if (goods == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Goods not found");
        }

        // Security Check: 只有发布者或管理员可以修改
        if (!goods.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        BeanUtils.copyProperties(request, goods);

        // 更新时也要重新处理图片 List -> JSON
        if (request.getImages() != null) {
            goods.setImages(gson.toJson(request.getImages()));

            // 如果更新导致封面图没了，也可以在这里补一个逻辑（可选）
            if (StringUtils.isBlank(goods.getCoverImage()) && !request.getImages().isEmpty()) {
                goods.setCoverImage(request.getImages().get(0));
            }
        }

        return this.updateById(goods);
    }

    @Override
    public List<GoodsVO> searchGoods(String keyword, Long categoryId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();

        // 只查询未删除且在售的商品
        queryWrapper.eq("status", 1);
        queryWrapper.eq("isDelete", 0); // 确保加上逻辑删除判断

        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(qw -> qw.like("title", keyword).or().like("description", keyword));
        }
        if (categoryId != null && categoryId > 0) {
            // 注意：这里使用的是字符串 "categoryId"，确保 Entity 里的 @TableField 已生效
            queryWrapper.eq("categoryId", categoryId);
        }

        queryWrapper.orderByDesc("createTime");

        List<Goods> goodsList = this.list(queryWrapper);

        // 填充 VO 信息 (卖家头像等)
        return goodsList.stream().map(this::getGoodsVO).collect(Collectors.toList());
    }

    @Override
    public boolean deleteGoods(long id, User loginUser, boolean isAdmin) {
        Goods goods = this.getById(id);
        if (goods == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 权限校验
        if (!isAdmin && !goods.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return this.removeById(id); // 逻辑删除 (前提是 application.yml 配置了 logic-delete-value)
    }

    @Override
    public IPage<Goods> listGoods(Integer pageNum, Integer pageSize, String keyword, Long categoryId) {
        Page<Goods> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(keyword), Goods::getTitle, keyword);
        queryWrapper.eq(categoryId != null && categoryId > 0, Goods::getCategoryId, categoryId);
        queryWrapper.eq(Goods::getStatus, 1);
        queryWrapper.eq(Goods::getIsDelete, 0);
        queryWrapper.orderByDesc(Goods::getCreateTime);

        return this.page(page, queryWrapper);
    }

    @Override
    public GoodsVO getGoodsDetail(long id, HttpServletRequest request) {
        // 1. 查商品
        Goods goods = this.getById(id);
        if (goods == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        // 2. 转 VO
        GoodsVO goodsVO = GoodsVO.objToVo(goods);

        // 3. 填充用户信息
        Long userId = goods.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                goodsVO.setUser(userVO);
            }
        }

        // 4. 填充分类名称
        Long categoryId = goods.getCategoryId();
        if (categoryId != null && categoryId > 0) {
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                goodsVO.setCategoryName(category.getName());
            }
        }

        return goodsVO;
    }

    @Override
    public List<GoodsVO> listMyGoods(User loginUser, Integer status) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());

        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("createTime");

        List<Goods> goodsList = this.list(queryWrapper);

        return goodsList.stream().map(goods -> {
            GoodsVO vo = GoodsVO.objToVo(goods);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean updateStatus(Long goodsId, Integer status, User loginUser) {
        Goods goods = this.getById(goodsId);
        if (goods == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (!goods.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        Goods updateGoods = new Goods();
        updateGoods.setId(goodsId);
        updateGoods.setStatus(status);
        return this.updateById(updateGoods);
    }

    /**
     * 辅助方法：将 Goods 转为 GoodsVO 并填充卖家信息
     */
    private GoodsVO getGoodsVO(Goods goods) {
        GoodsVO goodsVO = GoodsVO.objToVo(goods);
        // 查询卖家信息
        Long userId = goods.getUserId();
        if (userId != null && userId > 0) {
            User seller = userService.getById(userId);
            if (seller != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(seller, userVO);
                goodsVO.setUser(userVO);
            }
        }
        return goodsVO;
    }
}