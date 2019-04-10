package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.OrderDao;
import com.hjy.miaosha.domain.MiaoshaOrder;
import com.hjy.miaosha.domain.OrderInfo;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;


    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long id, long goodsId) {
            return orderDao.getMiaoshaOrderByUserIdGoodsId(id,goodsId);
    }

    @Transactional
    public OrderInfo insertOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insetMiaoshaOrder(miaoshaOrder);
        return orderInfo;
    }
}
