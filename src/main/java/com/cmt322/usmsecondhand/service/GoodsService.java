package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Goods;

/**
* @author 米老头
* @description 针对表【goods(Goods Table)】的数据库操作Service
* @createDate 2025-11-22 14:08:36
*/
public interface GoodsService extends IService<Goods> {
    // 发布商品
    boolean publishGoods(Goods goods, Long userId);

    // 更新商品
    boolean updateGoods(Goods goods, Long userId);

    // 删除商品（逻辑删除）
    boolean deleteGoods(Long goodsId, Long userId);

    // 获取商品详情
    GoodsVO getGoodsDetail(Long goodsId);

    // 根据分类获取商品列表
    List<GoodsVO> getGoodsByCategory(Long categoryId, Integer page, Integer size);

    // 搜索商品
    List<GoodsVO> searchGoods(String keyword, Integer page, Integer size);

    // 获取用户发布的商品
    List<GoodsVO> getUserGoods(Long userId);

    // 获取热门商品
    List<GoodsVO> getHotGoods(Integer limit);

    // 更新商品状态（如标记为已售出）
    boolean updateGoodsStatus(Long goodsId, Integer status, Long userId);
}
