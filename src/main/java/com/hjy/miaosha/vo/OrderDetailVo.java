package com.hjy.miaosha.vo;

import com.hjy.miaosha.domain.Goods;
import com.hjy.miaosha.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
    private Goods goods;
    private OrderInfo order;
}
