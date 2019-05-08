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


}
