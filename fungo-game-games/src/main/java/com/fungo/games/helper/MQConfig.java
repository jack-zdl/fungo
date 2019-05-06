package com.fungo.games.helper;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@Configuration
public class MQConfig {

    //topic 可以广播关键字匹配的队列
    public static final String TOPIC_QUEUE_GAME_INSERT = "topic.queue.game.insert";
    public static final String TOPIC_QUEUE_GAME_UPDATE = "topic.queue.game.update";

    public static final String TOPIC_EXCHANGE_GAME_INSERT = "topic.exchange.game.insert";
    public static final String TOPIC_EXCHANGE_GAME_UPDATE = "topic.exchange.game.update";

    public static final String TOPIC_KEY_GAME_INSERT = "topic.key.game.insert";
    public static final String TOPIC_KEY_GAME_UPDATE = "topic.key.game.update";

//    @Bean
//    public Binding topicBindingGameInsert(){
//        return BindingBuilder.bind(new Queue(TOPIC_QUEUE_GAME_INSERT,true))
//                .to(new TopicExchange(TOPIC_EXCHANGE_GAME_INSERT))
//                .with(TOPIC_KEY_GAME_INSERT);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
//    }
//
//    @Bean
//    public Binding topicBindingGameUpdate(){
//        return BindingBuilder.bind(new Queue(TOPIC_QUEUE_GAME_UPDATE,true))
//                .to(new TopicExchange(TOPIC_EXCHANGE_GAME_UPDATE))
//                .with(TOPIC_KEY_GAME_UPDATE);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
//    }
}
