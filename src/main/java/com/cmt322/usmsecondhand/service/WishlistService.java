package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Wishlist;
import com.cmt322.usmsecondhand.model.vo.WishlistVO;

import java.util.List;

/**
 * 收藏夹服务接口
 */
public interface WishlistService extends IService<Wishlist> {

    /**
     * 添加收藏
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return 是否成功
     */
    boolean addWishlist(Long userId, Long goodsId);

    /**
     * 移除收藏
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return 是否成功
     */
    boolean removeWishlist(Long userId, Long goodsId);

    /**
     * 获取用户的收藏列表（包含商品详细信息）
     *
     * @param userId 用户ID
     * @return 收藏商品列表 VO
     */
    List<WishlistVO> getUserWishlist(Long userId);

    boolean isCollected(Long userId, Long goodsId);
}