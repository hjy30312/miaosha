package com.hjy.miaosha.vo;

import com.hjy.miaosha.domain.User;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private User user;
}
