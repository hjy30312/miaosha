package com.hjy.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    /**
     * Direct模式 交换机Exchange
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.QUQUE)
    public void receive(String message) {
        log.info("receive message");
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUQUE1)
    public void receive1(String message) {

        log.info("receive topic queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUQUE2)
    public void receive2(String message) {

        log.info("receive topic queue2 message:" + message);
    }

}
