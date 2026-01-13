package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.mapper.WishlistMapper;
import com.cmt322.usmsecondhand.model.Wishlist;
import com.cmt322.usmsecondhand.model.vo.WishlistVO;
import com.cmt322.usmsecondhand.service.GoodsService;
import com.cmt322.usmsecondhand.service.UserService;
import com.cmt322.usmsecondhand.service.WishlistService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WishlistServiceImpl extends ServiceImpl<WishlistMapper, Wishlist> implements WishlistService {

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserService userService;

    @Override
    public boolean addWishlist(Long userId, Long goodsId) {
        // 防止重复收藏
        QueryWrapper<Wishlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        queryWrapper.eq("goodsID", goodsId);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return true; // 已经收藏过了，视为成功
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setGoodsId(goodsId);
        return this.save(wishlist);
    }

    @Override
    public boolean removeWishlist(Long userId, Long goodsId) {
        QueryWrapper<Wishlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        queryWrapper.eq("goodsID", goodsId);
        return this.remove(queryWrapper);
    }

    @Override
    public List<WishlistVO> getUserWishlist(Long userId) {
        // 1. 查出该用户所有收藏记录
        QueryWrapper<Wishlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        queryWrapper.orderByDesc("createTime");
        List<Wishlist> wishlistList = this.list(queryWrapper);

        if (wishlistList.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 提取所有 goodsId
        List<Long> goodsIds = wishlistList.stream().map(Wishlist::getGoodsId).collect(Collectors.toList());

        // 3. 批量查询商品信息
        List<Goods> goodsList = goodsService.listByIds(goodsIds);

        // 4. 组装 VO (需要把商品信息和卖家信息拼进去)
        return goodsList.stream().map(goods -> {
            WishlistVO vo = new WishlistVO();
            BeanUtils.copyProperties(goods, vo);

            vo.setGoodsId(goods.getId());
            
            // 查询卖家信息
            User seller = userService.getById(goods.getUserId());
            if (seller != null) {
                vo.setSellerId(seller.getId());
                vo.setSellerName(seller.getUsername());
                vo.setSellerAvatar(seller.getAvatarUrl());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isCollected(Long userId, Long goodsId) {
        QueryWrapper<Wishlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userID", userId);
        queryWrapper.eq("goodsID", goodsId);
        return this.count(queryWrapper) > 0;
    }
}