package com.cmt322.usmsecondhand.model.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品发布请求DTO
 */
@Data
public class GoodsPublishRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String coverImage;
    private Object images;
    private Integer condition;
    private String campus;
    private List<Integer> contactTypes;
    private Integer deliveryMethod;
    private String pickupLocation;
}