package com.fungo.games.helper;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
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
//    public static final String TOPIC_QUEUE_GAME_INSERT = "topic.queue.game.insert";
//    public static final String TOPIC_QUEUE_GAME_UPDATE = "topic.queue.game.update";

    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;


    // 这样做  rabbitmq会讲路由键也是direct.queue，队列名称
    public static final String DIRECT_QUEUE = "direct.queue";

    /**
     * fanout 只能申明明确的队列名
     */
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    //topic 可以广播关键字匹配的队列
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";

    public static final String TOPIC_EXCHANGE_GAME_INSERT = "topic.exchange.game.insert";
    public static final String TOPIC_EXCHANGE_GAME_UPDATE = "topic.exchange.game.update";
    /*public static final String TOPIC_EXCHANGE_COMMUNITY_INSERT = "topic.exchange.community.insert";
    public static final String TOPIC_EXCHANGE_GAME_TAG = "topic.exchange.game.tag";
    public static final String TOPIC_EXCHANGE_GAMERELEASELOG_INSERT = "topic.exchange.gamereleaselog.insert";
    public static final String TOPIC_EXCHANGE_BASACTION_INSERT = "topic.exchange.basaction.insert";*/
    public static final String TOPIC_EXCHANGE_BASACTION_INSERTALLCOLUMN = "topic.exchange.basaction.insertallcolumn";
    public static final String TOPIC_EXCHANGE_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID = "topic.exchange.basaction.selectoneandupdateallcolumnbyid";

    public static final String TOPIC_QUEUE_GAME_INSERT = "topic.queue.game.insert";
    public static final String TOPIC_QUEUE_GAME_UPDATE = "topic.queue.game.update";
    /*public static final String TOPIC_QUEUE_COMMUNITY_INSERT = "topic.queue.community.insert";
    public static final String TOPIC_QUEUE_GAME_TAG = "topic.queue.game.tag";
    public static final String TOPIC_QUEUE_GAMERELEASELOG_INSERT = "topic.queue.gamereleaselog.insert";
    public static final String TOPIC_QUEUE_BASACTION_INSERT = "topic.queue.basaction.insert";*/
    public static final String TOPIC_QUEUE_BASACTION_INSERTALLCOLUMN = "topic.queue.basaction.insertallcolumn";
    public static final String TOPIC_QUEUE_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID = "topic.queue.basaction.selectoneandupdateallcolumnbyid";

    public static final String TOPIC_KEY_GAME_INSERT = "topic.key.game.insert";
    public static final String TOPIC_KEY_GAME_UPDATE = "topic.key.game.update";
    /*public static final String TOPIC_KEY_COMMUNITY_INSERT = "topic.key.community.insert";
    public static final String TOPIC_KEY_GAME_TAG = "topic.key.game.tag";
    public static final String TOPIC_KEY_GAMERELEASELOG_INSERT = "topic.key.gamereleaselog.insert";
    public static final String TOPIC_KEY_BASACTION_INSERT = "topic.key.basaction.insert";*/
    public static final String TOPIC_KEY_BASACTION_INSERTALLCOLUMN = "topic.key.basaction.insertallcolumn";
    public static final String TOPIC_KEY_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID = "topic.key.basaction.selectoneandupdateallcolumnbyid";

    public static final String TOPIC_KEY1 = "topic.key1";
    public static final String TOPIC_KEY2 = "topic.#";

    public static final String HEADER_QUEUE = "header.queue";
    public static final String HEADER_EXCHANGE = "header.exchange";

//    /**
//     * Direct 模式 交换机Exchange
//     */
//    @Bean
//    public Queue directQueue(){
//        // 一个参数是名称，，另一个表示是否持久化
//        return new Queue(DIRECT_QUEUE,true);
//    }
//
//    /**-----------------fanout START----------------------------*/
//
//    /**
//     * Fanout Pattern.   类似于广播一样，将消息发送给和他绑定的队列
//     **/
//    @Bean
//    public Queue fanoutQueue1(){
//        return new Queue(FANOUT_QUEUE1,true);
//    }
//
//    @Bean
//    public Queue fanoutQueue2(){
//        return new Queue(FANOUT_QUEUE2,true);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//
//    /**
//     * 绑定 exchange and queue
//     */
//    @Bean
//    public Binding fanoutBinding1(){
//        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding fanoutBinding2(){
//        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
//    }
//
//    /**------------------fanout END ---------------------------*/

    /**------------------fanout START---------------------------*/

    /**
     * Topic Pattern.  绑定交换机时可以做匹配。 #：表示零个或多个单词。*：表示一个单词
     **/
//    @Bean
//    public Queue topicQueue1(){
//        return new Queue(TOPIC_QUEUE1,true);
//    }
//
//    @Bean
//    public Queue topicQueue2(){
//        return new Queue(TOPIC_QUEUE2,true);
//    }
    /***************************交换机**************************************/
    @Bean
    public TopicExchange topicExchangeGameInsert(){
        return new TopicExchange(TOPIC_EXCHANGE_GAME_INSERT);
    }

    @Bean
    public TopicExchange topicExchangeGameUpdate(){
        return new TopicExchange(TOPIC_EXCHANGE_GAME_UPDATE);
    }

    /*@Bean
    public TopicExchange topicExchangeCommunityInsert(){
        return new TopicExchange(TOPIC_EXCHANGE_COMMUNITY_INSERT);
    }

    @Bean
    public TopicExchange topicExchangeGameTag(){
        return new TopicExchange(TOPIC_EXCHANGE_GAME_TAG);
    }

    @Bean
    public TopicExchange topicExchangeGamereleaselogInsert(){
        return new TopicExchange(TOPIC_EXCHANGE_GAMERELEASELOG_INSERT);
    }

//    2019-05-10
    @Bean
    public TopicExchange topicExchangeBasActionInsert(){
        return new TopicExchange(TOPIC_EXCHANGE_BASACTION_INSERT);
    }*/
//    2019-05-10
    @Bean
    public TopicExchange topicExchangeBasActionInsertAllColumn(){
        return new TopicExchange(TOPIC_EXCHANGE_BASACTION_INSERTALLCOLUMN);
    }
    //    2019-05-10
    @Bean
    public TopicExchange topicExchangeBasActionSelectOneAndUpdateAllColumnById(){
        return new TopicExchange(TOPIC_EXCHANGE_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID);
    }




    /**
     * 绑定 exchange and queue
     * 设置是精准匹配还是模糊匹配
     */
//    @Bean
//    public Binding topicBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(TOPIC_KEY1);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
//    }
//    @Bean
//    public Binding topicBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(TOPIC_KEY2);  // 模糊匹配，匹配成功则发送到 TOPIC_QUEUE2队列
//    }

    /***  start ***/
    /********************创建Queue***********************************/
    @Bean
    public Queue topicQueueGameInsert(){
        return new Queue(TOPIC_QUEUE_GAME_INSERT,true);
    }
    @Bean
    public Queue topicQueueGameUpdate(){
        return new Queue(TOPIC_QUEUE_GAME_UPDATE,true);
    }
   /* @Bean
    public Queue topicQueueCommunityInsert(){
        return new Queue(TOPIC_QUEUE_COMMUNITY_INSERT,true);
    }
    @Bean
    public Queue topicQueueGameTag(){
        return new Queue(TOPIC_QUEUE_GAME_TAG,true);
    }

    @Bean
    public Queue topicQueueGamereleaselogInsert(){
        return new Queue(TOPIC_QUEUE_GAMERELEASELOG_INSERT,true);
    }
    //    2019-05-10
    @Bean
    public Queue topicQueueBasActionInsert(){
        return new Queue(TOPIC_QUEUE_BASACTION_INSERT,true);
    }*/
    //    2019-05-10
    @Bean
    public Queue topicQueueBasActionInsertAllColumn(){
        return new Queue(TOPIC_QUEUE_BASACTION_INSERTALLCOLUMN,true);
    }
    //    2019-05-10
    @Bean
    public Queue topicQueueBasActionSelectOneAndUpdateAllColumnById(){
        return new Queue(TOPIC_QUEUE_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID,true);
    }

    /*****************************************绑定Queue******************************************************************/

    @Bean
    public Binding topicBindingGameUpdate(){
        return BindingBuilder.bind(topicQueueGameUpdate()).to(topicExchangeGameUpdate()).with(TOPIC_KEY_GAME_UPDATE);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }

    @Bean
    public Binding topicBindingGameInsert(){
        return BindingBuilder.bind(topicQueueGameInsert()).to(topicExchangeGameInsert()).with(TOPIC_KEY_GAME_INSERT);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }

    /*@Bean
    public Binding topicBindingCommunityInsert(){
        return BindingBuilder.bind(topicQueueCommunityInsert()).to(topicExchangeCommunityInsert()).with(TOPIC_KEY_COMMUNITY_INSERT);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }

    @Bean
    public Binding topicBindingGameTag(){
        return BindingBuilder.bind(topicQueueGameTag()).to(topicExchangeGameTag()).with(TOPIC_KEY_GAME_TAG);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }

    @Bean
    public Binding topicBindingGamereleaselogInsert(){
        return BindingBuilder.bind(topicQueueGamereleaselogInsert()).to(topicExchangeGamereleaselogInsert()).with(TOPIC_KEY_GAMERELEASELOG_INSERT);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }
//    2019-05-10
    @Bean
    public Binding topicBindingBasActionInsert(){
        return BindingBuilder.bind(topicQueueBasActionInsert()).to(topicExchangeBasActionInsert()).with(TOPIC_KEY_BASACTION_INSERT);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }*/
//    2019-05-10
    @Bean
    public Binding topicBindingBasActionInsertAllColumn(){
        return BindingBuilder.bind(topicQueueBasActionInsertAllColumn()).to(topicExchangeBasActionInsertAllColumn()).with(TOPIC_KEY_BASACTION_INSERTALLCOLUMN);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }
//    2019-05-10
    @Bean
    public Binding topicBindingBasActionSelectOneAndUpdateAllColumnById(){
        return BindingBuilder.bind(topicQueueBasActionSelectOneAndUpdateAllColumnById()).to(topicExchangeBasActionSelectOneAndUpdateAllColumnById()).with(TOPIC_KEY_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID);  // 精确匹配, 匹配成功则发送到 TOPIC_QUEUE1队列
    }


    /**-------------------fanout END--------------------------*/


    /**------------------header START---------------------------*/

//    /**
//     * Header Pattern.  交换机 Exchange
//     **/
//    @Bean
//    public Queue headerQueue(){
//        return new Queue(HEADER_QUEUE,true);
//    }
//    @Bean
//    public HeadersExchange headersExchange(){
//        return new HeadersExchange(HEADER_EXCHANGE);
//    }
//    @Bean
//    public Binding headerBinding(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("header1","value1");
//        map.put("header2","value2");
//        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();   // whereXxx() 方法代表了匹配规则
//    }
    /**-------------------header END--------------------------*/

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setAddresses(addresses + ":" + port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost(virtualHost);
//        /** 如果要进行消息回调，则这里必须要设置为true */
//        connectionFactory.setPublisherConfirms(publisherConfirms);
//        return connectionFactory;
//    }

}
