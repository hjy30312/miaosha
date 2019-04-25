package com.hjy.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {

    public static final String QUQUE = "queue";
    public static final String TOPIC_QUQUE1 = "topic.queue1";
    public static final String TOPIC_QUQUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";

    @Bean
    public Queue queue() {
        // 持久队列
        return new Queue(MQConfig.QUQUE,true);
    }


    @Bean
    public Queue topicQueue1() {
        // 持久队列
        return new Queue(MQConfig.TOPIC_QUQUE1,true);
    }

    @Bean
    public Queue topicQueue2() {
        // 持久队列
        return new Queue(MQConfig.TOPIC_QUQUE2,true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MQConfig.TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /**
     * Fanout模式 交换机Exchange
     */
    public FanoutExchange



}
