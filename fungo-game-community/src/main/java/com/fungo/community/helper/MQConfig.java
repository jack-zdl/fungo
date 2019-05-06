package com.fungo.community.helper;

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

    public static final String TOPIC_QUEUE_COMMUNITY_INSERT = "topic.queue.community.insert";

}
