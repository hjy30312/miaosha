package com.hjy.miaosha.rabbitmq;


import com.hjy.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }

//
//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUQUE, msg);
//    }
//
//    public void sendTopic(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY1, msg + "1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY2, msg + "2");
//    }
//
//
//    public void sendFanout(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
//    }
//
//
//    public void sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message:" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1", "value1");
//        properties.setHeader("header2", "value2");
//        Message obj = new Message(msg.getBytes(), properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//
//    }

}
