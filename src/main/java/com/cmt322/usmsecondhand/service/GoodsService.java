package com.cmt322.usmsecondhand.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmt322.usmsecondhand.model.Goods;
import com.cmt322.usmsecondhand.model.User;
import com.cmt322.usmsecondhand.model.request.GoodsPublishRequest;
import com.cmt322.usmsecondhand.model.request.GoodsUpdateRequest;
import com.cmt322.usmsecondhand.model.vo.GoodsVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface GoodsService extends IService<Goods> {

    /**
     * 发布商品
     */
    long publishGoods(GoodsPublishRequest request, User loginUser);

    /**
     * 更新商品
     */
    boolean updateGoods(GoodsUpdateRequest request, User loginUser);

    /**
     * 搜索商品
     */
    List<GoodsVO> searchGoods(String keyword, Long categoryId);

    /**
     * 获取商品详情
     */
    GoodsVO getGoodsDetail(long id, HttpServletRequest request);

    // ★★★ 删除了那个多余的 publishGoods(..., Long userId) ★★★

    /**
     * 首页分页列表
     */
    IPage<Goods> listGoods(Integer pageNum, Integer pageSize, String keyword, Long categoryId);

    /**
     * 删除商品
     */
    boolean deleteGoods(long id, User loginUser, boolean isAdmin);

    // 在接口中添加
    List<GoodsVO> listMyGoods(User loginUser, Integer status);
    boolean updateStatus(Long goodsId, Integer status, User loginUser);
}