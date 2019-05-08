package com.hjy.miaosha.domain;


import lombok.Data;

/**
 * 商品
 */
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;


}
