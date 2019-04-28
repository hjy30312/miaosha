package com.hjy.miaosha.service;


import com.hjy.miaosha.domain.MiaoshaOrder;
import com.hjy.miaosha.domain.OrderInfo;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.MiaoshaKey;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    /**
     * 减库存 下订单 写入秒杀订单
     *
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goods) {
        boolean flag = goodsService.reduceStock(goods);
        log.info("service:miaosha flag= " + flag);
        if (flag) {
            return orderService.insertOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }


    public long getMiaoshaResult(Long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
        if (order != null) { //秒杀成功
            log.info("秒杀成功");
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;  //卖光了
            } else {
                return 0;
            }
        }

    }


}
