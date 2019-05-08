package com.hjy.miaosha.rabbitmq;

import com.hjy.miaosha.domain.User;
import lombok.Data;

@Data
public class MiaoshaMessage {
    private User user;
    private long goodsId;
    private long date;


}
