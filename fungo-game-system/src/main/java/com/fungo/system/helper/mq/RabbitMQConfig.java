package com.fungo.system.helper.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
//@Configuration
public class RabbitMQConfig {
//    private static final String topicExchangeName = "topic-exchange";
//    private static final String fanoutExchange = "fanout-exchange";
//    private static final String headersExchange = "headers-exchange";
//
//    private static final String queueName = "cord";
//
//    //声明队列
//    @Bean
//    public Queue queue() {
//        //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete)
//        return new Queue("cord", false, true, true);
//    }
//
//    //声明队列
//    @Bean
//    public Queue topicQueue() {
//        //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete)
//        return new Queue("topicQueue", false, true, true);
//    }
//
//    //声明Topic交换机
////    @Bean
////    TopicExchange topicExchange1() {
////        return new TopicExchange(topicExchangeName);
////    }
////
////    //将队列与Topic交换机进行绑定，并指定路由键
////    @Bean
////    Binding topicBinding(Queue queue, TopicExchange topicExchange) {
////        return BindingBuilder.bind(queue).to(topicExchange).with("org.cord.#");
////    }
//
//    //声明fanout交换机
//    @Bean
//    FanoutExchange fanoutExchange() {
//        return new FanoutExchange(fanoutExchange);
//    }
//
//    //将队列与fanout交换机进行绑定
//    @Bean
//    Binding fanoutBinding(Queue queue, FanoutExchange fanoutExchange) {
//        return BindingBuilder.bind(queue).to(fanoutExchange);
//    }
//
//    //声明Headers交换机
//    @Bean
//    HeadersExchange headersExchange() {
//        return new HeadersExchange(headersExchange);
//    }
//
//    //将队列与headers交换机进行绑定
//    @Bean
//    Binding headersBinding(Queue queue, HeadersExchange headersExchange) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("First","A");
//        map.put("Fourth","D");
//        //whereAny表示部分匹配，whereAll表示全部匹配
////        return BindingBuilder.bind(queue).to(headersExchange).whereAll(map).match();
//        return BindingBuilder.bind(queue).to(headersExchange).whereAny(map).match();
//    }

}
