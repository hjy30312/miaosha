package com.hjy.miaosha.rabbitmq;

import com.hjy.miaosha.domain.MiaoshaOrder;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.MiaoshaService;
import com.hjy.miaosha.service.OrderService;
import com.hjy.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        MiaoshaMessage mes = RedisService.stringToBean(message, MiaoshaMessage.class);
        Long goodsId = mes.getGoodsId();
        User user = mes.getUser();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return;
        }
        //大并发情况下  可能发生相同时间戳的情况   获取顺序队列  拿到用户id

        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 生成秒杀订单
        miaoshaService.miaosha(user, goodsVo);

    }



    /**
     * Direct模式 交换机Exchange
     *
     * @param message
     */
//    @RabbitListener(queues = MQConfig.QUQUE)
//    public void receive(String message) {
//        log.info("receive message");
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUQUE1)
//    public void receive1(String message) {
//        log.info("receive topic queue1 message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUQUE2)
//    public void receive2(String message) {
//        log.info("receive topic queue2 message:" + message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUQUE1)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info("receive header queue2 message:" + new String(message));
//    }

}
