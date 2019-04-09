package com.hjy.miaosha.vo;

import com.hjy.miaosha.domain.Goods;

import java.util.Date;

/**
 * 商品信息
 */
public class GoodsVo extends Goods {

    private Integer stockCount;
    private Double miaoshaPrice;
    private Date startDate;
    private Date endDate;

    @Override
    public String toString() {
        return "GoodsVo{" +
                "stockCount=" + stockCount +
                ", miaoshaPrice=" + miaoshaPrice +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public GoodsVo() {
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
