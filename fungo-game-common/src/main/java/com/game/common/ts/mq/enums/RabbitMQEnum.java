package com.game.common.ts.mq.enums;


/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:RabbitMQ 交换机和队列名称定义<br />
 * @Date: Create in 2018/5/8
 */
public class RabbitMQEnum {

    public enum Exchange {
        EXCHANGE_FANOUT(1, "msgFungo_Fanout"),
        EXCHANGE_TOPIC(2, "msgFungo_Topic"),
        EXCHANGE_DIRECT(3, "msgFungo_Direct");

        private int code;
        private String name;

        Exchange(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
        return name;
    }
}

    /**
     * 消息业务领域类型:
     * 1 系统
     * 2 用户
     * 3 社区-文章
     * 4 社区-心情
     * 5 游戏
     * 6 首页
     */
    public enum MQQueueName {
        //默认
        MQ_QUEUE_TOPIC_NAME_DEFAULT(99, "msgFungoTopicQueue"),

        //默认
        MQ_QUEUE_DIRECT_NAME_DEFAULT(88, "msgFungoDirectQueue"),

        //1 系统-系统队列
        MQ_QUEUE_TOPIC_NAME_SYSTEM(1,"msgFungoTopicQueue_system"),

        //2 系统-用户队列
        MQ_QUEUE_TOPIC_NAME_SYSTEM_USER(2,"msgFungoTopicQueue_system_user"),

        //3 社区-文章队列
        MQ_QUEUE_TOPIC_NAME_COMMUNITY_POST(3, "msgFungoTopicQueue_community_POST"),

        //4 社区-心情队列
        MQ_QUEUE_TOPIC_NAME_COMMUNITY_MOOD(4, "msgFungoTopicQueue_community_MOOD"),

        //5 游戏
        MQ_QUEUE_TOPIC_NAME_GAMES(5, "msgFungoTopicQueue_Games"),

        //6 首页
        MQ_QUEUE_TOPIC_NAME_PORTAL(6, "msgFungoTopicQueue_portal");


        private int code;
        private String name;

        MQQueueName(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    public enum QueueRouteKey {

        //默认
        QUEUE_ROUTE_KEY_DEFAULT(99, "msgFungo.#"),

        //1 系统-系统队列
        QUEUE_ROUTE_KEY_TOPIC_SYSTEM(1, "msgFungoTopicSystem.#"),

        //2 系统-用户队列
        QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER(2, "msgFungoTopicSystemUser.#"),

        //3 社区-文章队列
        QUEUE_ROUTE_KEY_TOPIC_COMMUNITY_POST(3, "msgFungoTopicCommunityPost.#"),

        //4 社区-心情队列
        QUEUE_ROUTE_KEY_TOPIC_COMMUNITY_MOOD(4, "msgFungoTopicCommunityMood.#"),

        //5 游戏
        QUEUE_ROUTE_KEY_TOPIC_GAMES(5, "msgFungoTopicGames.#"),

        //6 首页
        QUEUE_ROUTE_KEY_TOPIC_PORTAL(6, "msgFungoTopicPortal.#");

        private int code;
        private String name;

        QueueRouteKey(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}
